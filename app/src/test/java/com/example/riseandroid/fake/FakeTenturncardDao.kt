package com.example.riseandroid.fake

import com.example.riseandroid.data.entitys.TenturncardDao
import com.example.riseandroid.data.entitys.TenturncardEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTenturncardDao : TenturncardDao {
        // In-memory storage for the entities
        private val tenturncards = MutableStateFlow<List<TenturncardEntity>>(emptyList())

        override fun getAllTenturncards(): Flow<List<TenturncardEntity>> {
            TODO("Not yet implemented")
        }

        override suspend fun getTenturncardById(id: Int): TenturncardEntity? {
            TODO("Not yet implemented")
        }

        override suspend fun insertTenturncards(cards: List<TenturncardEntity>) {
            TODO("Not yet implemented")
        }

        override suspend fun addTenturncard(card: TenturncardEntity) {
            tenturncards.value += card
        }

        override suspend fun updateTenturncard(card: TenturncardEntity) {
            TODO("Not yet implemented")
        }

        override suspend fun deleteTenturncard(card: TenturncardEntity) {
            tenturncards.value = tenturncards.value.filter { it.id != card.id }
        }

        override suspend fun deleteAllTenturncards() {
            TODO("Not yet implemented")
        }
    }