package com.example.riseandroid.repository

import com.example.riseandroid.data.entitys.tenturncard.TenturncardEntity
import com.example.riseandroid.data.entitys.tenturncard.TenturncardResponse
import com.example.riseandroid.model.Tenturncard
import kotlinx.coroutines.flow.Flow

interface ITenturncardRepository {
    suspend fun getTenturncards(): Flow<List<Tenturncard>>
    fun addTenturncard(activationCode : String): Flow<ApiResource<TenturncardEntity>>
    suspend fun editTenturncard(toUpdateCard : Tenturncard) : Flow<ApiResource<TenturncardResponse>>
}