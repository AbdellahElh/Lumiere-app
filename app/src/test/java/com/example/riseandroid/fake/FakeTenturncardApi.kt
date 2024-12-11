package com.example.riseandroid.fake

import coil.request.SuccessResult
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.network.ResponseTenturncard
import com.example.riseandroid.network.TenturncardApi
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.mock.Calls

class FakeTenturncardApi : TenturncardApi {


    private val tenturncards = mutableListOf<ResponseTenturncard>(
        ResponseTenturncard(
            id = 1,
            amountLeft = 10,
            expirationDate = "5",
            purchaseDate = "5",
            isActivated = false,
            activationCode = "testCode"
        )
    )
    override suspend fun getTenturncards(): List<ResponseTenturncard> {
        return tenturncards
    }


    override fun addTenturncard(activationCode: String): Call<Unit> {
        return if(activationCode == "testCode") {
            Calls.response(Response.success(Unit))
        }
        else{
            Calls.response(Response.error(400, "Invalid activation code".toResponseBody(null)))
        }
    }

    override fun updateTenturncard(activationCode: String): Call<Unit> {

    }
}