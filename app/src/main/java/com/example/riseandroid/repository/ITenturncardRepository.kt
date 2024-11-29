package com.example.riseandroid.repository

import com.example.riseandroid.data.entitys.TenturncardEntity
import com.example.riseandroid.model.Tenturncard
import kotlinx.coroutines.flow.Flow

interface ITenturncardRepository {
    suspend fun getTenturncards(authToken: String): Flow<List<Tenturncard>>
    suspend fun addTenturncard(authToken: String, activationCode : String): Flow<ApiResource<TenturncardEntity>>
}