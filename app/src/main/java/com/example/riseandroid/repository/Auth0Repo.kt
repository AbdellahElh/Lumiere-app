package com.example.riseandroid.repository

import android.util.Log
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import com.auth0.android.result.DatabaseUser
import com.example.riseandroid.network.auth0.Auth0Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Auth0Repo( private val authentication: AuthenticationAPIClient,
                 private val credentialsManager: CredentialsManager,
                 private val authApi: Auth0Api
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
    ): Flow<APIResource<Credentials>> = flow {
        emit(APIResource.Loading<Credentials>())

        val response = performSignUp(email, password, connection)

        if (response != null) {
            emit(APIResource.Success(response))
        } else {
            emit(APIResource.Error("Issue with Auth API during sign-up"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun logout() {
        credentialsManager.clearCredentials()
        Log.d("API", "User logged out. Credentials cleared.")
    }

    private suspend fun performSignUp(
        email: String,
        password: String,
        connection: String
    ): Credentials? {
        return withContext(Dispatchers.IO) {
            try {
                val databaseUser = createUserAsync(email, password, connection)

                if (databaseUser != null) {
                    return@withContext performLogin(email, password)
                } else {
                    // Als de gebruiker niet kon worden aangemaakt, geef null terug
                    null
                }
            } catch (e: Exception) {
                // Log de fout of handel deze af
                e.printStackTrace()
                null
            }
        }
    }

    private suspend fun createUserAsync(
        email: String,
        password: String,
        connection: String
    ): DatabaseUser? {
        return suspendCoroutine { continuation ->
            authentication.createUser(
                email,
                password,
                connection,
                connection = "Username-Password-Authentication",
                userMetadata = null
            )
                .start(object : Callback<DatabaseUser, AuthenticationException> {
                    override fun onSuccess(result: DatabaseUser) {
                        continuation.resume(result)
                    }

                    override fun onFailure(error: AuthenticationException) {
                        continuation.resumeWithException(error)
                    }
                })
        }
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