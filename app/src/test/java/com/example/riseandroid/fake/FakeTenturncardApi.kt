package com.example.riseandroid.fake

import android.provider.CallLog
import com.example.riseandroid.data.entitys.tenturncard.TenturncardResponse
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.network.TenturncardApi
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.mock.Calls

class FakeTenturncardApi : TenturncardApi {
    override suspend fun getTenturncards(): List<TenturncardResponse> {
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

    override fun editTenturncard(toUpdateCard: TenturncardResponse): Call<Unit> {
        return if(toUpdateCard.amountLeft == 5) {
            Calls.response(Response.success(Unit))
        }
        else{
            Calls.response(Response.error(400, "Invalid activation code".toResponseBody(null)))
        }
    }

    override fun updateTenturncard(activationCode: String): Call<Unit> {
        TODO("Not yet implemented")
    }
}