package com.example.riseandroid.fake

import com.auth0.android.Auth0
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.IAuthRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import okhttp3.internal.userAgent
import java.util.Date



// Simulating the data structure for Credentials.
class FakeAuth0Repo : IAuthRepo {
    override val auth0: Auth0
        get() = TODO("Not yet implemented")

    override suspend fun getCredentials(): Flow<ApiResource<Credentials>> = flow {
        try {
            // Emit loading state initially
            emit(ApiResource.Loading())

            val credentials = Credentials(
                idToken = "fake_id_token_123",
                accessToken = "fake_access_token_456",
                refreshToken = "fake_refresh_token_789",
                type = "Bearer",
                expiresAt = Date(System.currentTimeMillis() + 3600 * 1000), // 1 hour from now
                scope = "openid profile email",
            )
            // Emit the fetched credentials as a success
            emit(ApiResource.Success(credentials))
        } catch (e: Exception) {
            // Handle exceptions and emit an error state
            emit(ApiResource.Error("Failed to fetch credentials: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getLoggedInId(): Flow<ApiResource<Int>> = flow {
       emit(ApiResource.Success(1))
    }

    override suspend fun signUp(
        email: String,
        password: String,
        connection: String
    ): Flow<ApiResource<Credentials>> {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun sendForgotPasswordEmail(email: String): Flow<ApiResource<Void>> {
        TODO("Not yet implemented")
    }

    override suspend fun performLogin(
        userName: String,
        password: String
    ): Flow<ApiResource<Credentials>> {
        TODO("Not yet implemented")
    }

}