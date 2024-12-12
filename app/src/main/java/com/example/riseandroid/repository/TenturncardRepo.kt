package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.tenturncard.TenturncardDao
import com.example.riseandroid.data.entitys.tenturncard.TenturncardEntity
import com.example.riseandroid.data.entitys.tenturncard.TenturncardResponse
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.network.TenturncardApi
import com.example.riseandroid.util.asEntity
import com.example.riseandroid.util.asExternalModel
import com.example.riseandroid.util.asResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.awaitResponse

class TenturncardRepository(
    private val tenturncardApi: TenturncardApi,
    private val tenturncardDao: TenturncardDao,
    private val authrepo: IAuthRepo,
) : ITenturncardRepository {

    override suspend fun getTenturncards(): Flow<List<Tenturncard>> {
        return tenturncardDao.getAllTenturncards()
            .map { entities -> entities.map(TenturncardEntity::asExternalModel) }
            .onStart {
                withContext(Dispatchers.IO) {
                    refreshTenturncards()
                }
            }


    }

    private suspend fun refreshTenturncards() {
        try {

            val cardsFromApi = tenturncardApi.getTenturncards()
            val cardsAsEntities = cardsFromApi.map { it.asEntity() }
            tenturncardDao.deleteAllTenturncards()
            tenturncardDao.insertTenturncards(cardsAsEntities)

        } catch (e: Exception) {
            Log.e("TenturncardRepo", "Error refreshing cards")
        }
    }


    override fun addTenturncard(activationCode: String): Flow<ApiResource<TenturncardEntity>> =
        flow {
            emit(ApiResource.Loading())
            val newTenturncard = TenturncardEntity(
                amountLeft = 10,
                ActivationCode = activationCode,
                UserTenturncardId = getLoggedInUserId(),
                purchaseDate = null,
                expirationDate = null
            )
            try {
                val response = tenturncardApi.addTenturncard(activationCode)
                if (response.awaitResponse().isSuccessful) {
                    emit(ApiResource.Success(newTenturncard))
                    tenturncardDao.addTenturncard(newTenturncard)
                } else {
                    emit(ApiResource.Error<TenturncardEntity>("Toevoegen gefaald"))
                    tenturncardDao.deleteTenturncard(newTenturncard)
                }
            } catch (e: Exception) {
                emit(
                    ApiResource.Error<TenturncardEntity>(
                        message = e.toString() ?: "Er was een onverwachte fout"
                    )
                )
                tenturncardDao.deleteTenturncard(newTenturncard)
            }
        }.flowOn(Dispatchers.IO)


    override suspend fun minOneUpdateCardById(activationCode: String): Flow<ApiResource<TenturncardResponse>> = flow {
        emit(ApiResource.Loading())


        val apiResponse = tenturncardApi.updateTenturncard(activationCode).awaitResponse()

        if (apiResponse.isSuccessful) {
            try {
                val existingCard = tenturncardDao.getTenturncardByActivationCode(activationCode)
                if (existingCard == null) {
                    emit(ApiResource.Error("Tenturncard niet gevonden"))
                    return@flow
                }
                tenturncardDao.updateTenturncard(
                    ActivationCode = activationCode,
                    amountLeft = (existingCard.amountLeft - 1)
                )
                val updatedCard = tenturncardDao.getTenturncardByActivationCode(activationCode)
                //    val cards = tenturncardDao.getAllTenturncards()

                if (updatedCard != null) {
                    emit(ApiResource.Success(updatedCard.asResponse()))
                }
            } catch (e: Exception) {
                emit(ApiResource.Error<TenturncardResponse>("Er is een fout opgetreden: ${e.localizedMessage}"))
            }

        } else {
            emit(ApiResource.Error<TenturncardResponse>("Fout: lege response van de API"))
        }

    }.flowOn(Dispatchers.IO)


    override suspend fun editTenturncard(toUpdateCard: Tenturncard): Flow<ApiResource<TenturncardResponse>> = flow {
        // Grab the card before updating it in case you need to reverse the action
        val backupCard = tenturncardDao.getTenturncardById(toUpdateCard.id)
        // Create an entity version
        val cardEntity = toUpdateCard.asEntity()
        cardEntity.UserTenturncardId = getLoggedInUserId()
        // Create a response version
        val cardResponse = toUpdateCard.asResponse()
        cardResponse.accountId = getLoggedInUserId()
        // Emit the loading state
        emit(ApiResource.Loading())
        try {

            // Send the request to the api
            val response = tenturncardApi.editTenturncard(cardResponse).awaitResponse()
            // Emit the correct state based on the api response
            if (response.isSuccessful) {
                tenturncardDao.updateTenturncard(
                    cardEntity.ActivationCode,
                    amountLeft = cardEntity.amountLeft,
                )
                emit(ApiResource.Success(cardResponse))
            }
            else{
                emit(ApiResource.Error(getErrorMessage(response)?: "Bewerken van kaart gefaald"))
                if (backupCard != null) {
                    tenturncardDao.updateTenturncard(backupCard.ActivationCode,backupCard.amountLeft)
                }
            }

        }catch (e : Exception) {
            emit(ApiResource.Error(e.message ?: "Bewerken van kaart gefaald"))
            if (backupCard != null) {
                tenturncardDao.updateTenturncard(backupCard.ActivationCode,backupCard.amountLeft)
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun getErrorMessage(response : Response<Unit>): String? {
        val errorBody = response.errorBody()
        if (errorBody != null) {
            val errorBodyString = errorBody.string() // Convert the error body to a string
            try {
                val jsonObject = JSONObject(errorBodyString)
                val message = jsonObject.getString("Message") // Extract the "Message" field
                return message
            } catch (e: JSONException) {
                return null
            }
        } else {
            return null
        }
    }


    suspend fun getLoggedInUserId(): Int {
        var accountId: Int? = null
        authrepo.getLoggedInId().collect { resource ->
            when (resource) {
                is ApiResource.Error -> throw IllegalStateException("De gebruiker moet ingelogd zijn")
                is ApiResource.Initial -> throw IllegalStateException("Unexpected state: ApiResource.Initial")
                is ApiResource.Loading -> null

                is ApiResource.Success -> {
                    accountId = resource.data
                }
            }
        }
        return accountId ?: throw IllegalStateException("De gebruiker moet ingelogd zijn")
    }
}
