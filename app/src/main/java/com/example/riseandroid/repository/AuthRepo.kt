package com.example.riseandroid.repository

import android.content.Context
import android.util.Log
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.result.Credentials
import com.example.riseandroid.network.auth0.Auth0Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.Date


interface IAuthRepo {

    val auth0: Auth0
    suspend fun getCredentials(userName: String, password:String): Flow<APIResource<Credentials>>
    suspend fun signUp(email: String, password: String, connection: String): Flow<APIResource<Credentials>>
    suspend fun logout()
    suspend fun sendForgotPasswordEmail(email: String): Flow<APIResource<Void>>
}


class AuthRepo(
    context: Context,
    val authApi: Auth0Api,
    override val auth0: Auth0
) : IAuthRepo {

    private val authentication = AuthenticationAPIClient(auth0)
    private val credentialsManager = CredentialsManager(
        authentication,
        SharedPreferencesStorage(context)
    )

    override suspend fun getCredentials(
        userName: String,
        password: String
    ): Flow<APIResource<Credentials>> = flow {
        val loadingResource = APIResource.Loading<Credentials>()
        emit(loadingResource)

        val call = authApi.getAccessToken(
            userName = userName,
            pwd = password
        )

        val response = withContext(Dispatchers.IO) {
            call.execute()
        }

        if (response.isSuccessful) {
            val authorizeResponse = response.body()

            if (authorizeResponse != null) {
                val credentials = Credentials(
                    idToken = authorizeResponse.access_token,
                    accessToken = authorizeResponse.access_token,
                    type = authorizeResponse.token_type,
                    refreshToken = authorizeResponse.refresh_token,
                    expiresAt = Date(System.currentTimeMillis() + (authorizeResponse.expires_in * 1000L)),
                    scope = null
                )

                Log.d("API", "Login successful. Access token: ${credentials.accessToken}")
                emit(APIResource.Success(credentials))
            } else {
                Log.e("API", "Error processing login response")
                emit(APIResource.Error<Credentials>("Failed to process login. Response was empty."))
            }
        } else {
            Log.e("API", "Login unsuccessful: ${response.errorBody()?.string()}")
            emit(APIResource.Error<Credentials>("Login failed: ${response.errorBody()?.string()}"))
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
        val loadingResource = APIResource.Loading<Void>()
        emit(loadingResource)

        try {
            val call = authApi.sendForgotPasswordEmail(email)

            val response = withContext(Dispatchers.IO) {
                call.execute()
            }

            if (response.isSuccessful) {
                Log.d("API", "Forgot password email sent successfully for email: $email")
                emit(APIResource.Success<Void>(null)) // Geen data terug, maar het is succesvol
            } else {
                Log.e("API", "Failed to send forgot password email: ${response.errorBody()?.string()}")
                emit(APIResource.Error<Void>("Failed to send password reset email: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Log.e("API", "Error sending forgot password email: ${e.message}")
            emit(APIResource.Error<Void>("Error sending password reset email: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
}



