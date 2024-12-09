package com.example.riseandroid.network

import com.example.riseandroid.data.entitys.tenturncard.TenturncardResponse
import com.example.riseandroid.model.Tenturncard
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TenturncardApi {

    @GET("/api/tenturncard")
    suspend fun getTenturncards(): List<Tenturncard>

    @POST("/api/tenturncard/add/{activationCode}")
    suspend fun addTenturncard(@Path("activationCode") activationCode: String) : Call<Unit>

    @POST("/api/tenturncard/edit")
    suspend fun editTenturncard(@Body toUpdateCard : TenturncardResponse): Call<Unit>
}
