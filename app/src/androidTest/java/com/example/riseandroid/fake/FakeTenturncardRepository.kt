package com.example.riseandroid.fake

import com.example.riseandroid.data.entitys.tenturncard.TenturncardEntity
import com.example.riseandroid.data.entitys.tenturncard.TenturncardResponse
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.ITenturncardRepository
import com.example.riseandroid.util.asResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTenturncardRepository : ITenturncardRepository {
    private val toAddCard = TenturncardEntity(
        amountLeft = 10,
        ActivationCode = "succesCode",
        UserTenturncardId = 0,
        purchaseDate = null,
        expirationDate = null
    )
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

    override suspend fun getTenturncards(): Flow<List<Tenturncard>> {
        return flow {
            kotlinx.coroutines.delay(1000)
            emit(fakeCards)
        }
    }

    override suspend fun editTenturncard(toUpdateCard: Tenturncard): Flow<ApiResource<TenturncardResponse>> {
        return flow {
            // Simulate a delay for API-like behavior
            kotlinx.coroutines.delay(1000)

            // Check if the card exists in the list
            val updatedCards = fakeCards.map { card ->
                if (card.id == toUpdateCard.id) toUpdateCard else card
            }

            // Create a response for the updated card
            val updatedCard = updatedCards.find { it.id == toUpdateCard.id }
            if (updatedCard != null) {
                emit(ApiResource.Success(toUpdateCard.asResponse()))
            } else {
                emit(ApiResource.Error("Card with ID ${toUpdateCard.id} not found"))
            }
        }
    }


    override fun addTenturncard(activationCode: String): Flow<ApiResource<TenturncardEntity>> {
        return flow {
            emit(ApiResource.Loading())
            if (activationCode == "succesCode") {
                emit(ApiResource.Success(toAddCard))
            }
            else {
                emit(ApiResource.Error("Something went wrong"))
            }
        }
    }
}
