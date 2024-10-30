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
    suspend fun getCredentials(userName: String, password:String): Flow<APIResource<Credentials>>
    suspend fun signUp(email: String, password: String, connection: String): Flow<APIResource<Credentials>>
    suspend fun logout()
    suspend fun sendForgotPasswordEmail(email: String): Flow<APIResource<Void>>
}

class Auth0Repo( private val authentication: AuthenticationAPIClient,
                 private val credentialsManager: CredentialsManager,
                 private val authApi: Auth0Api,
                 override val auth0: Auth0
) : IAuthRepo {

    private suspend fun performLogin(userName: String, password: String): Credentials? {
        return withContext(Dispatchers.IO) {
            try {
                authentication.login(userName, password).execute()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun getCredentials(
        userName: String,
        password: String
    ): Flow<APIResource<Credentials>> = flow {
        emit(APIResource.Loading<Credentials>())

        val response = performLogin(userName, password)

        if (response != null) {
            emit(APIResource.Success(response))
        } else {
            emit(APIResource.Error("Issue with Auth API"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun signUp(
        email: String,
        password: String,
        connection: String
    ): Flow<APIResource<Credentials>> = flow<APIResource<Credentials>> {
        val loadingResource=APIResource.Loading<Credentials>()
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
            emit(APIResource.Success<Credentials>(signUpResponse))
        } else {
            emit(APIResource.Error(errorMessage ?: "SignUp error"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun logout() {
        credentialsManager.clearCredentials()
        Log.d("API", "User logged out. Credentials cleared.")
    }

    override suspend fun sendForgotPasswordEmail(email: String): Flow<APIResource<Void>> = flow {
        emit(APIResource.Loading<Void>()) // Emit loading state

        try {
            val call = authApi.sendForgotPasswordEmail(email) // Auth0 API gebruiken

            withContext(Dispatchers.IO) {
                val response = call.execute()
                if (response.isSuccessful) {
                    emit(APIResource.Success(null) as APIResource<Void>)
                } else {
                    emit(APIResource.Error<Void>("Failed to send password reset email: ${response.errorBody()?.string()}"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(APIResource.Error<Void>("Failed to send password reset email: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

}