package com.example.riseandroid.fake

import com.auth0.android.Auth0
import com.auth0.android.result.Credentials
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.IAuthRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

class FakeAuthRepo : IAuthRepo {

    private val fakeCredentials = Credentials(
        accessToken = "fakeAccessToken",
        idToken = "fakeIdToken",
        refreshToken = "fakeRefreshToken",
        type = "",
        expiresAt = Date(System.currentTimeMillis() + 60 * 60 * 1000),
        scope = ""
    )

    override val auth0: Auth0
        get() = throw UnsupportedOperationException("Auth0 is not used in FakeAuthRepo")

    override suspend fun getCredentials(
        userName: String,
        password: String
    ): Flow<ApiResource<Credentials>> {
        return flow {
            emit(ApiResource.Success(fakeCredentials))
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        connection: String
    ): Flow<ApiResource<Credentials>> {
        return flow {
            emit(ApiResource.Loading())
            delay(500)
            emit(ApiResource.Success(fakeCredentials))
        }
    }

    override suspend fun logout() {
        // Logout
    }

    override suspend fun sendForgotPasswordEmail(email: String): Flow<ApiResource<Void>> {
        return flow {
            emit(ApiResource.Loading())
            delay(500)
            emit(ApiResource.Success(null))
        }
    }
}
