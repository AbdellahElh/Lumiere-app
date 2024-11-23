package com.example.riseandroid.repository

import com.auth0.android.result.Credentials
import com.example.riseandroid.data.entitys.TenturncardDao
import com.example.riseandroid.data.entitys.TenturncardEntity
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.network.TenturncardApi
import com.example.riseandroid.ui.screens.account.AuthState
import com.example.riseandroid.ui.screens.account.AuthViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.internal.userAgent

class TenturncardRepository(
    private val tenturncardApi: TenturncardApi,
    private val tenturncardDao: TenturncardDao,
    private val authrepo : IAuthRepo,
) : ITenturncardRepository {
    override suspend fun getTenturncards(): List<Tenturncard> {
        return try {
            val apiResponse = tenturncardApi.getTenturncards()
            val entities = apiResponse.map {
                TenturncardEntity(
                    id = it.id,
                    amountLeft = it.amountLeft,
                    purchaseDate = it.purchaseDate,
                    expirationDate = it.expirationDate,
                    IsActivated = it.IsActivated,
                    ActivationCode = it.ActivationCode,
                    UserTenturncardId = null,
                )
            }
            tenturncardDao.deleteAllTenturncards()
            tenturncardDao.insertTenturncards(entities)

            entities.map { entity ->
                Tenturncard(
                    id = entity.id,
                    amountLeft = entity.amountLeft,
                    purchaseDate = entity.purchaseDate ?: "N/A",
                    expirationDate = entity.expirationDate ?: "N/A",
                    IsActivated = entity.IsActivated,
                    ActivationCode = entity.ActivationCode,
                )
            }
        } catch (e: Exception) {

            tenturncardDao.getAllTenturncards().map { entity ->
                Tenturncard(
                    id = entity.id,
                    amountLeft = entity.amountLeft,
                    purchaseDate = entity.purchaseDate ?: "N/A",
                    expirationDate = entity.expirationDate ?: "N/A",
                    IsActivated = entity.IsActivated,
                    ActivationCode = entity.ActivationCode,
                )
            }
        }
    }
    override suspend fun addTenturncard(activationCode : String): Flow<ApiResource<TenturncardEntity>> = flow {
        emit(ApiResource.Loading())
        try {
            //Send a request to the external api to add a new tenturncard to the user
            val response = tenturncardApi.addTenturncard(activationCode)
            if (response.isSuccess){
                //add the tenturncard to the offline database or local storage
                var newTenturncard = TenturncardEntity(
                    amountLeft = 10,
                    ActivationCode = activationCode,
                    UserTenturncardId = getLoggedInUserId(),
                    purchaseDate = null,
                    expirationDate = null
                )
                tenturncardDao.addTenturncard(newTenturncard)
                emit(ApiResource.Success(newTenturncard))
            }
        }catch (e : Exception){
            emit(ApiResource.Error<TenturncardEntity>(message = e.message ?: "Failed to add tenturncard"))
        }
    }

    suspend fun getLoggedInUserId() : Int {
        val resource = authrepo.getCredentials().first()
        val idToken = resource.data?.user?.getId()
        if (idToken != null) {
            return idToken.toInt()
        }
        else{
            throw Exception("De gebruiker moet ingelogd zijn")
        }
    }
    //Repo spreekt db aan om daar een flow aan te vragen
    //DAO geeft flow objecten

    //Repo spreekt de db aan via dao's (sync functies) om flow te verkrijgen
    //Api om async tasks op te halen!!
    // -> tutorial unit 6

}
