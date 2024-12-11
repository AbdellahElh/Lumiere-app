package com.example.riseandroid.network

import com.example.riseandroid.model.Tenturncard
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TenturncardApi {

    @GET("/api/tenturncard")
    suspend fun getTenturncards(): List<ResponseTenturncard>

    @POST("/api/tenturncard/add/{activationCode}")
    fun addTenturncard(@Path("activationCode") activationCode: String) : Call<Unit>

    @POST("/api/Tenturncard/update/{activationCode}")
    fun updateTenturncard(@Path("activationCode") activationCode : String) : Call<Unit>
}
data class ResponseTenturncard(
    val id: Int,
    val amountLeft: Int,
    val purchaseDate: String,
    val expirationDate: String,
    val isActivated: Boolean,
    val activationCode: String,
)