package com.example.riseandroid.repository

import com.example.riseandroid.data.entitys.TenturncardDao
import com.example.riseandroid.data.entitys.TenturncardEntity
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.network.TenturncardApi
import com.example.riseandroid.ui.screens.account.AuthState
import com.example.riseandroid.ui.screens.account.AuthViewModel
import okhttp3.internal.userAgent

class TenturncardRepository(
    private val tenturncardApi: TenturncardApi,
    private val tenturncardDao: TenturncardDao
) {
    suspend fun getTenturncards(): List<Tenturncard> {
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
    suspend fun addTenturncard(activationCode : String) : Result<Tenturncard>{
        try {
            //Send a request to the external api to add a new tenturncard
            val response = tenturncardApi.addTenturncard(activationCode)
            if (response.isSuccess){
                //add the tenturncard to the offline database or local storage
                var newTenturncard = TenturncardEntity(
                    amountLeft = 10,
                    ActivationCode = activationCode,
                    UserTenturncardId = //todo
                )
                tenturncardDao.addTenturncard(newTenturncard)
            }
        }catch (e : Exception){
            //todo
        }
    }
    //Repo spreekt db aan om daar een flow aan te vragen
    //DAO geeft flow objecten

    //Repo spreekt de db aan via dao's (sync functies) om flow te verkrijgen
    //Api om async tasks op te halen!!
    // -> tutorial unit 6

}
