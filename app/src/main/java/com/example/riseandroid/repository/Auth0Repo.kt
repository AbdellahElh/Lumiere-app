package com.example.riseandroid.repository

import android.util.Log
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.result.Credentials

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


class Auth0Repo(val authentication: AuthenticationAPIClient): IAuthRepo{

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


}