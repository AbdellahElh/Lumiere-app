package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.TenturncardDao
import com.example.riseandroid.data.entitys.TenturncardEntity
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.network.TenturncardApi
import com.example.riseandroid.util.asEntity
import com.example.riseandroid.util.asExternalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class TenturncardRepository(
    private val tenturncardApi: TenturncardApi,
    private val tenturncardDao: TenturncardDao,
    private val authrepo: IAuthRepo,
) : ITenturncardRepository {

    override suspend fun getTenturncards(): Flow<List<Tenturncard>> {
        return tenturncardDao.getAllTenturncards()
            .map { entities -> entities.map(TenturncardEntity::asExternalModel) }
            .onEach { cards ->
                cards.forEach { cards ->
                    Log.d("MoviesList", cards.toString())
                }
            }
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

    override fun updateTenturncard(id: Int): Flow<ApiResource<TenturncardEntity>> = flow {
        emit(ApiResource.Loading())

        try {

            val existingCard = tenturncardDao.getTenturncardById(id)

            if (existingCard == null) {
                emit(ApiResource.Error("Tenturncard niet gevonden"))
                return@flow
            }


            val apiResponse = tenturncardApi.updateTenturncard(id).awaitResponse()

            if (apiResponse.isSuccessful) {

                tenturncardDao.updateTenturncard(existingCard)

                emit(ApiResource.Success(existingCard))
            } else {
                emit(ApiResource.Error<TenturncardEntity>("API-update mislukt met status: ${apiResponse.code()}"))
            }
        } catch (e: Exception) {
            emit(ApiResource.Error<TenturncardEntity>("Er is een fout opgetreden: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun getLoggedInUserId(): Int {
        var accountId: Int? = null
        authrepo.getLoggedInId().collect { resource ->
            when (resource) {
                is ApiResource.Error -> throw IllegalStateException("De gebruiker moet ingelogd zijn")
                is ApiResource.Initial -> throw IllegalStateException("Unexpected state: ApiResource.Initial")
                is ApiResource.Loading -> {
                    // Optionally handle the loading state, or simply ignore it.
                }

                is ApiResource.Success -> {
                    accountId = resource.data
                }
            }
        }
                return accountId ?: throw IllegalStateException("De gebruiker moet ingelogd zijn")
    }
}
