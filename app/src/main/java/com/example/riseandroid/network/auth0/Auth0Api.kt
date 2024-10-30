package com.example.riseandroid.network.auth0

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Auth0Api {

    companion object {
        const val BASE_URL = "https://alpayozer.eu.auth0.com/"
    }


    @POST("/dbconnections/change_password")
    @FormUrlEncoded
    fun sendForgotPasswordEmail(
        @Field("email") email: String,
        @Field("connection") connection: String = "Username-Password-Authentication"
    ): Call<Void>
}