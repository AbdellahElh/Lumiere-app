package com.example.riseandroid.fake

import com.example.riseandroid.network.auth0.Auth0Api
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FakeAuth0Api : Auth0Api {

    override fun sendForgotPasswordEmail(email: String, connection: String): Call<Void> {
        // Returns a dummy Call that always succeeds with a null response
        return object : Call<Void> {
            override fun enqueue(callback: Callback<Void>) {
                // Trigger onResponse with a success Response
                callback.onResponse(this, Response.success(null))
            }

            override fun isExecuted(): Boolean = false

            override fun clone(): Call<Void> = this

            override fun isCanceled(): Boolean = false

            override fun cancel() {
                // No operation needed for a fake call
            }

            override fun execute(): Response<Void> {
                // Synchronous success response
                return Response.success(null)
            }

            override fun request(): Request {
                return Request.Builder()
                    .url(Auth0Api.BASE_URL)
                    .build()
            }

            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }
    }
}
