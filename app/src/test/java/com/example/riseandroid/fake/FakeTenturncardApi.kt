package com.example.riseandroid.fake

import coil.request.SuccessResult
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.network.TenturncardApi
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.mock.Calls

class FakeTenturncardApi : TenturncardApi {
    override suspend fun getTenturncards(): List<Tenturncard> {
        TODO("Not yet implemented")
    }

    override fun addTenturncard(activationCode: String): Call<Unit> {
        return if(activationCode == "testCode") {
            Calls.response(Response.success(Unit))
        }
        else{
            Calls.response(Response.error(400, "Invalid activation code".toResponseBody(null)))
        }
    }
}