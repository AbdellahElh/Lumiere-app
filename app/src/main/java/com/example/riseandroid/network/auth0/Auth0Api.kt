package com.example.riseandroid.network.auth0

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Auth0Api {

    companion object {
        const val BASE_URL = "https://alpayozer.eu.auth0.com/"
    }

    /*
    * {"access_token":"eyJhbGci...","expires_in":86400,"token_type":"Bearer"}
    * */
    @FormUrlEncoded
    @POST("oauth/token")
    fun getAccessToken(@Field("username") userName: String,
                       @Field("audience") audience: String= "https://alpayozer.eu.auth0.com/userinfo",
                       @Field("grant_type") grantType: String = "password",
                       @Field("client_id") clientId: String = "it8XxtD6gPwh8XQODS3vrZ4FrtfZoTOG",
                       @Field("password") pwd: String
    ): Call<AuthorizeResponse>


    @POST("/dbconnections/change_password")
    @FormUrlEncoded
    fun sendForgotPasswordEmail(
        @Field("email") email: String,
        @Field("connection") connection: String = "Username-Password-Authentication"
    ): Call<Void>
}