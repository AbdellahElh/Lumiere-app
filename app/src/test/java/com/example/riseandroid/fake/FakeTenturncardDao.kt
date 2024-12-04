package com.example.riseandroid.fake

import com.example.riseandroid.data.entitys.TenturncardDao
import com.example.riseandroid.data.entitys.TenturncardEntity
import kotlinx.coroutines.flow.Flow

class FakeTenturncardDao : TenturncardDao {
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
        return Unit
    }

    override suspend fun updateTenturncard(card: TenturncardEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTenturncard(card: TenturncardEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTenturncards() {
        TODO("Not yet implemented")
    }
}