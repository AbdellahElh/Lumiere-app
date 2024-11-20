package com.example.riseandroid.network

import com.example.riseandroid.model.Tenturncard
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TenturncardApi {

    @GET("/api/tenturncards")
    suspend fun getTenturncards(): List<Tenturncard>


}
