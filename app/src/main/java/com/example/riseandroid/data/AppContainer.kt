package com.example.riseandroid.data

import com.example.riseandroid.data.entitys.AccountRepository
import com.example.riseandroid.data.entitys.OfflineAccountRepository
import retrofit2.Retrofit
import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.data.lumiere.NetworkMoviesRepository
import com.example.riseandroid.network.LumiereApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.example.riseandroid.repository.Auth0Repo
import com.example.riseandroid.repository.IAuthRepo


interface AppContainer {
    val moviesRepository: MoviesRepository
    val authRepo: IAuthRepo

}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val BASE_URL = "https://dev-x5ihmi3j64lha722.us.auth0.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: LumiereApiService by lazy {
        retrofit.create(LumiereApiService::class.java)
    }

    override val moviesRepository: MoviesRepository by lazy {
        NetworkMoviesRepository(retrofitService)
    }


    //the client provided by auth0
    var auth0: Auth0 = Auth0(context)
    var authentication: AuthenticationAPIClient = AuthenticationAPIClient(auth0)

    override val authRepo: IAuthRepo by lazy {
        Auth0Repo(authentication)
    }


//    override val accountRepository : AccountRepository by lazy {
//        OfflineAccountRepository(Database.getDatabase(context))
//    }

}