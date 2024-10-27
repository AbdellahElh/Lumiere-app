package com.example.riseandroid.repository

import com.auth0.android.result.Credentials
import com.auth0.android.result.DatabaseUser
import kotlinx.coroutines.flow.Flow

interface IAuthRepo {
    suspend fun signUp(email: String, password: String, connection: String): Flow<APIResource<Credentials>>
}


