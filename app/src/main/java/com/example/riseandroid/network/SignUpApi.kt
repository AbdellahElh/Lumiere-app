package com.example.riseandroid.network

import com.example.riseandroid.model.RegisterDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {

    @POST("api/Account/register")
    fun registerUser(@Body registerDto: RegisterDto): Call<Void>
}