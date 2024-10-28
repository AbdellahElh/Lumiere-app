package com.example.riseandroid.network.auth0

import kotlinx.serialization.Serializable

@Serializable
data class AuthorizeResponse(
    val access_token: String,
    val expires_in: Long,
    val token_type: String,
    val refresh_token: String?
) {
}