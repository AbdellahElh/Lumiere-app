package com.example.riseandroid.model

import android.util.Log
import com.auth0.android.jwt.JWT


data class User(val idToken: String? = null) {

    private val TAG = "User"

    var id = ""
    var name = ""
    var email = ""
    var emailVerified = ""
    var picture = ""
    var updatedAt = ""

    init {
        if (idToken != null) {
            try {
                val jwt = JWT(idToken ?: "")

                id = jwt.subject ?: ""
                name = jwt.getClaim("name").asString() ?: ""
                email = jwt.getClaim("email").asString() ?: ""
                emailVerified = jwt.getClaim("email_verified").asString() ?: ""
                picture = jwt.getClaim("picture").asString() ?: ""
                updatedAt = jwt.getClaim("updated_at").asString() ?: ""
            } catch (error: com.auth0.android.jwt.DecodeException) {
                Log.e(TAG, "Error occurred trying to decode JWT: ${error.toString()} ")
            }
        } else {
            Log.d(TAG, "User is logged out - instantiating empty User object.")
        }
    }

}