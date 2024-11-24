package com.example.riseandroid.fake

import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.network.TenturncardApi

class FakeTenturncardApi : TenturncardApi {
    override suspend fun getTenturncards(): List<Tenturncard> {
        TODO("Not yet implemented")
    }

    override suspend fun addTenturncard(activationCode: String): Result<Unit> {
        if(activationCode == "testCode") {
            return Result.success(Unit)
        }
        else{
            return Result.failure(IllegalArgumentException())
        }
    }
}