package com.example.riseandroid.repository

import android.util.Log
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.result.Credentials
import com.example.riseandroid.model.RegisterDto
import com.example.riseandroid.network.SignUpApi
import com.example.riseandroid.network.auth0.Auth0Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

interface IAuthRepo {

    val auth0: Auth0
    suspend fun getCredentials(): Flow<ApiResource<Credentials>>
    suspend fun signUp(email: String, password: String, connection: String): Flow<ApiResource<Credentials>>
    suspend fun logout()
    suspend fun sendForgotPasswordEmail(email: String): Flow<ApiResource<Void>>
    suspend fun performLogin(userName: String, password: String): Flow<ApiResource<Credentials>>
    suspend fun getLoggedInId() : Flow<ApiResource<Int>>
}

class Auth0Repo( private val authentication: AuthenticationAPIClient,
                 private val credentialsManager: CredentialsManager,
                 private val authApi: Auth0Api,
                 private val signUpApi: SignUpApi,
                 override val auth0: Auth0
) : IAuthRepo {

    override suspend fun performLogin(userName: String, password: String): Flow<ApiResource<Credentials>> = flow {
        try {
            val credentials = authentication.login(userName, password)
                .setScope("openid profile email offline_access")
                .setAudience("https://api.gent5.com")
                .setRealm("Username-Password-Authentication")
                .validateClaims()
                .execute()
            credentialsManager.saveCredentials(credentials)
            Log.d("Auth0Repo", "Credentials opgeslagen: ${credentials.accessToken}")
            emit(ApiResource.Success(credentials))
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

    override suspend fun getLoggedInId(): Flow<ApiResource<Int>> = flow {
        val resource = getCredentials().first()
        val idToken = resource.data?.user?.getId()
        val authIdSplit = idToken?.split("|")
        val accountId = authIdSplit?.get(1)?.toInt()
        if (idToken != null) {
            emit(ApiResource.Success<Int>(accountId))
        }
        else{
            emit(ApiResource.Error<Int>("Gebruiker moet ingelogd zijn"))
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        connection: String
    ): Flow<ApiResource<Credentials>> = flow<ApiResource<Credentials>> {
        emit(ApiResource.Loading<Credentials>())

        try {
            val registerDto = RegisterDto(email = email, password = password)
            val signUpResponse = withContext(Dispatchers.IO) {
                signUpApi.registerUser(registerDto).execute()
            }

            if (signUpResponse.isSuccessful) {
                emitAll(performLogin(email, password))
            } else {
                val errorBody = signUpResponse.errorBody()?.string() ?: "Unknown error"
                Log.e("Error", "Error body: $errorBody")

                val regex = """"Message":"\\u0027(.*?)\\u0027""".toRegex()
                val matchResult = regex.find(errorBody)

                val errorMessage = matchResult?.groups?.get(1)?.value?.replace("\\u0027", "'")?.trim()
                    ?: "Unknown error"

                emit(ApiResource.Error<Credentials>(errorMessage))

            }

        }catch (e: IOException) {
            emit(ApiResource.Error<Credentials>("Geen netwerkverbinding"))
        }catch (e: Exception) {
            emit(ApiResource.Error<Credentials>("Sign-up failed: ${e.message}"))
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