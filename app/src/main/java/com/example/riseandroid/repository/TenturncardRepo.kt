package com.example.riseandroid.repository

import com.example.riseandroid.data.entitys.TenturncardDao
import com.example.riseandroid.data.entitys.TenturncardEntity
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.network.TenturncardApi

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
                    expirationDate = it.expirationDate
                )
            }
            tenturncardDao.deleteAllTenturncards()
            tenturncardDao.insertTenturncards(entities)

            entities.map { entity ->
                Tenturncard(
                    id = entity.id,
                    amountLeft = entity.amountLeft,
                    purchaseDate = entity.purchaseDate ?: "N/A",
                    expirationDate = entity.expirationDate ?: "N/A"
                )
            }
        } catch (e: Exception) {

            tenturncardDao.getAllTenturncards().map { entity ->
                Tenturncard(
                    id = entity.id,
                    amountLeft = entity.amountLeft,
                    purchaseDate = entity.purchaseDate ?: "N/A",
                    expirationDate = entity.expirationDate ?: "N/A"
                )
            }
        }
    }
}
