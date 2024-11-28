package com.example.riseandroid.fake

import com.example.riseandroid.data.entitys.TenturncardEntity
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.ITenturncardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTenturncardRepository : ITenturncardRepository {

    private val fakeCards = listOf(
        Tenturncard(
            id = 1,
            amountLeft = 5,
            purchaseDate = "2024-01-01",
            expirationDate = "2024-12-31",
            IsActivated = true,
            ActivationCode = "ABC123"
        ),
        Tenturncard(
            id = 2,
            amountLeft = 8,
            purchaseDate = "2024-05-15",
            expirationDate = "2025-06-30",
            IsActivated = false,
            ActivationCode = "XYZ456"
        ),
        Tenturncard(
            id = 3,
            amountLeft = 12,
            purchaseDate = "2023-08-01",
            expirationDate = "2024-08-01",
            IsActivated = true,
            ActivationCode = "DEF789"
        )
    )

    override suspend fun getTenturncards(authToken: String): Flow<List<Tenturncard>> {
        return flow {
            kotlinx.coroutines.delay(1000)
            emit(fakeCards)
        }
    }

    override suspend fun addTenturncard(activationCode: String): Flow<ApiResource<TenturncardEntity>> {
        TODO("Not yet implemented")
    }
}
