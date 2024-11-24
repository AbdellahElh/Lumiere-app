package com.example.riseandroid.repository

import android.util.Log
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.result.Credentials
import com.example.riseandroid.network.auth0.Auth0Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface IAuthRepo {

    val auth0: Auth0
    suspend fun getCredentials(): Flow<ApiResource<Credentials>>
    suspend fun signUp(email: String, password: String, connection: String): Flow<ApiResource<Credentials>>
    suspend fun logout()
    suspend fun sendForgotPasswordEmail(email: String): Flow<ApiResource<Void>>
    suspend fun performLogin(userName: String, password: String): Flow<ApiResource<Credentials>>
}

class Auth0Repo( private val authentication: AuthenticationAPIClient,
                 private val credentialsManager: CredentialsManager,
                 private val authApi: Auth0Api,
                 override val auth0: Auth0
) : IAuthRepo {

    override suspend fun performLogin(userName: String, password: String): Flow<ApiResource<Credentials>> = flow {
        try {
            val credentials = authentication.login(userName, password)
                .setScope("openid profile email offline_access")
                //.setAudience("https://api.gent5.com/")
                .execute()
            credentialsManager.saveCredentials(credentials)
            emit(ApiResource.Success<Credentials>(credentials))
        } catch (e: Exception) {
            emit(ApiResource.Error<Credentials>(e.message ?: "Login failed"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getCredentials(): Flow<ApiResource<Credentials>> = flow {
        emit(ApiResource.Loading<Credentials>())
        try {
            val credentials = credentialsManager.awaitCredentials()
            if (credentials != null){
                emit(ApiResource.Success<Credentials>(credentials))
            }
            else {
                emit(ApiResource.Error<Credentials>("No credentials found"))
            }
        }catch (e : Exception){
            emit(ApiResource.Error<Credentials>(e.message ?: "Failed to get credentials"))
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun signUp(
        email: String,
        password: String,
        connection: String
    ): Flow<ApiResource<Credentials>> = flow<ApiResource<Credentials>> {
        val loadingResource=ApiResource.Loading<Credentials>()
        emit(loadingResource)

        var errorMessage: String? = null
        val signUpCall = authentication.signUp(email, password, email, connection)
        val signUpResponse: Credentials? = withContext(Dispatchers.IO) {
            try {
                signUpCall.execute()
            } catch (e: Exception) {
                Log.e("Auth0Repo", "Sign up failed", e)
                errorMessage = " Dit account bestaat al"
                null
            }
        }

        if (signUpResponse != null) {
            emit(ApiResource.Success<Credentials>(signUpResponse))
        } else {
            emit(ApiResource.Error(errorMessage ?: "SignUp error"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun logout() {
        credentialsManager.clearCredentials()
        Log.d("API", "User logged out. Credentials cleared.")
    }

    override suspend fun sendForgotPasswordEmail(email: String): Flow<ApiResource<Void>> = flow {
        emit(ApiResource.Loading<Void>()) // Emit loading state

        try {
            val call = authApi.sendForgotPasswordEmail(email) // Auth0 API gebruiken

            withContext(Dispatchers.IO) {
                val response = call.execute()
                if (response.isSuccessful) {
                    emit(ApiResource.Success(null) as ApiResource<Void>)
                } else {
                    emit(ApiResource.Error<Void>("Failed to send password reset email: ${response.errorBody()?.string()}"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ApiResource.Error<Void>("Failed to send password reset email: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

}