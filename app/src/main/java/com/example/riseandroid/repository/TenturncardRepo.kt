package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.TenturncardDao
import com.example.riseandroid.data.entitys.TenturncardEntity
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.network.TenturncardApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class TenturncardRepository(
    private val tenturncardApi: TenturncardApi,
    private val tenturncardDao: TenturncardDao,
    private val authrepo: IAuthRepo,
) : ITenturncardRepository {

     override suspend fun getTenturncards(authToken: String): List<Tenturncard> {
        return try {

            val authHeader = "Bearer $authToken"
            // Call the API with the token
            val apiResponse = tenturncardApi.getTenturncards(authHeader)
            val entities = apiResponse.map {
                TenturncardEntity(
                    id = it.id,
                    amountLeft = it.amountLeft,
                    purchaseDate = it.purchaseDate,
                    expirationDate = it.expirationDate,
                    IsActivated = it.IsActivated,
                    ActivationCode = it.ActivationCode,
                    UserTenturncardId = null
                )
            }

            // Update the local database with the new data
            tenturncardDao.deleteAllTenturncards()
            tenturncardDao.insertTenturncards(entities)

            // Map entities to the model for the UI
            entities.map { entity ->
                Tenturncard(
                    id = entity.id,
                    amountLeft = entity.amountLeft,
                    purchaseDate = entity.purchaseDate ?: "N/A",
                    expirationDate = entity.expirationDate ?: "N/A",
                    IsActivated = entity.IsActivated,
                    ActivationCode = entity.ActivationCode
                )
            }
        } catch (e: Exception) {
            // Fallback to local database if API call fails
            tenturncardDao.getAllTenturncards().map { entity ->
                Tenturncard(
                    id = entity.id,
                    amountLeft = entity.amountLeft,
                    purchaseDate = entity.purchaseDate ?: "N/A",
                    expirationDate = entity.expirationDate ?: "N/A",
                    IsActivated = entity.IsActivated,
                    ActivationCode = entity.ActivationCode
                )
            }
        }
    }

    // Helper to get the auth token
    private suspend fun getAuthToken(): String {
        val flowResult = authrepo.getCredentials().firstOrNull()
        val authToken = flowResult?.data?.accessToken // Access token from credentials
        return authToken ?: throw IllegalStateException("No authentication token found. User must be logged in.")
    }



    override suspend fun addTenturncard(activationCode: String): Flow<ApiResource<TenturncardEntity>> = flow {
        emit(ApiResource.Loading())
        try {
            // Send a request to the external API to add a new tenturncard to the user
            val response = tenturncardApi.addTenturncard(activationCode)
            if (response.isSuccess) {
                // Add the tenturncard to the offline database or local storage
                val newTenturncard = TenturncardEntity(
                    amountLeft = 10,
                    ActivationCode = activationCode,
                    UserTenturncardId = getLoggedInUserId(),
                    purchaseDate = null,
                    expirationDate = null
                )
                tenturncardDao.addTenturncard(newTenturncard)
                emit(ApiResource.Success(newTenturncard))
            } else {
                emit(ApiResource.Error<TenturncardEntity>("Failed to add tenturncard"))
            }
        } catch (e: Exception) {
            emit(ApiResource.Error<TenturncardEntity>(message = e.message ?: "Failed to add tenturncard"))
        }
    }


    suspend fun getLoggedInUserId(): Int {
        val flowResult = authrepo.getLoggedInId().firstOrNull()
        // If the flow result is null or the data is null, throw an exception
        val accountId = flowResult?.data ?: throw IllegalStateException("De gebruiker moet ingelogd zijn")
        return accountId
    }
}
