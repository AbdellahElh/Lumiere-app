package com.example.riseandroid.data

import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.data.lumiere.NetworkMoviesRepository
import com.example.riseandroid.data.lumiere.NetworkProgramRepository
import com.example.riseandroid.data.lumiere.ProgramRepository
import com.example.riseandroid.network.LumiereApiService
import com.example.riseandroid.network.auth0.Auth0Api
import com.example.riseandroid.repository.Auth0Repo
import com.example.riseandroid.repository.IAuthRepo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface AppContainer {
    val programRepository: ProgramRepository

    val moviesRepository: MoviesRepository
    val authApiService: Auth0Api
    val authRepo: IAuthRepo
//    val accountRepository: AccountRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val BASE_URL = "https://alpayozer.eu.auth0.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: LumiereApiService by lazy {
        retrofit.create(LumiereApiService::class.java)
    }

    override val moviesRepository: MoviesRepository by lazy {
        NetworkMoviesRepository(retrofitService)
    }
    override val programRepository: ProgramRepository by lazy {
        NetworkProgramRepository(retrofitService)
    }

    override val authApiService: Auth0Api by lazy {
        retrofit.create(Auth0Api::class.java)
    }

    var auth0: Auth0 = Auth0(context)
    var authentication: AuthenticationAPIClient = AuthenticationAPIClient(auth0)

    private val credentialsManager: CredentialsManager by lazy {
        val storage = SharedPreferencesStorage(context)
        CredentialsManager(authentication, storage)
    }


    override val authRepo: IAuthRepo by lazy {
        Auth0Repo(
            authentication = authentication,
            credentialsManager = credentialsManager,
            authApi = authApiService,
            auth0 = auth0
        )
    }

//    override val accountRepository : AccountRepository by lazy {
//        OfflineAccountRepository(Database.getDatabase(context))
//    }

}