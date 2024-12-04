package com.example.riseandroid.network

import com.example.riseandroid.model.Tenturncard
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface TenturncardApi {

    @GET("/api/tenturncard")
    suspend fun getTenturncards(
        @Header("Authorization") authToken: String
    ): List<Tenturncard>
    @POST("/api/add/{activationCode}")
    suspend fun addTenturncard(
        @Path("activationCode") activationCode: String,
    ) : Result<Unit>
}
