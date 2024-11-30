package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.TenturncardDao
import com.example.riseandroid.data.entitys.TenturncardEntity
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.network.TenturncardApi
import com.example.riseandroid.util.asEntity
import com.example.riseandroid.util.asExternalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

class TenturncardRepository(
    private val tenturncardApi: TenturncardApi,
    private val tenturncardDao: TenturncardDao,
    private val authrepo: IAuthRepo,
) : ITenturncardRepository {

    override suspend fun getTenturncards(authToken: String):  Flow<List<Tenturncard>> {
        val authHeader = "Bearer $authToken"
        return tenturncardDao.getAllTenturncards()
            .map { entities -> entities.map(TenturncardEntity::asExternalModel) }
            .onEach { cards ->
                cards.forEach { cards ->
                    Log.d("MoviesList", cards.toString())
                }}
            .onStart {
                withContext(Dispatchers.IO) {
                    refreshTenturncards(authHeader)
                }
            }


    }

    private suspend fun refreshTenturncards(authToken: String) {
        try {

            val cardsFromApi = tenturncardApi.getTenturncards(
                authToken = authToken
            )
            val cardsAsEntities = cardsFromApi.map { it.asEntity() }
            tenturncardDao.deleteAllTenturncards()
            tenturncardDao.insertTenturncards(cardsAsEntities)

        } catch (e: Exception) {
            Log.e("TenturncardRepo", "Error refreshing cards")
        }
    }

    private suspend fun getAuthToken(): String {
        val flowResult = authrepo.getCredentials().firstOrNull()
        val authToken = flowResult?.data?.accessToken
        return authToken ?: throw IllegalStateException("No authentication token found. User must be logged in.")
    }



    override suspend fun addTenturncard(activationCode: String): Flow<ApiResource<TenturncardEntity>> = flow {
        emit(ApiResource.Loading())
        val newTenturncard = TenturncardEntity(
            amountLeft = 10,
            ActivationCode = activationCode,
            UserTenturncardId = getLoggedInUserId(),
            purchaseDate = null,
            expirationDate = null
        )
        try {
            //tenturncardDao.addTenturncard(newTenturncard)
            val response = tenturncardApi.addTenturncard(activationCode)
            if (response.isSuccess) {
                emit(ApiResource.Success(newTenturncard))
            } else {
                emit(ApiResource.Error<TenturncardEntity>("Toevoegen gefaald"))
                tenturncardDao.deleteTenturncard(newTenturncard)
            }
        } catch (e: Exception) {
            emit(ApiResource.Error<TenturncardEntity>(message = e.message ?: "Er was een onverwachte fout"))
            tenturncardDao.deleteTenturncard(newTenturncard)
        }
    }


    suspend fun getLoggedInUserId(): Int {
        val flowResult = authrepo.getLoggedInId().firstOrNull()
        val accountId = flowResult?.data ?: throw IllegalStateException("De gebruiker moet ingelogd zijn")
        return accountId
    }
}