package com.example.riseandroid.data

import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.example.riseandroid.R
import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.data.lumiere.NetworkMoviesRepository
import com.example.riseandroid.network.LumiereApiService
import com.example.riseandroid.data.lumiere.NetworkProgramRepository
import com.example.riseandroid.data.lumiere.NetworkTicketRepository

import com.example.riseandroid.data.lumiere.ProgramRepository
import com.example.riseandroid.data.lumiere.TicketRepository
import com.example.riseandroid.network.MoviesApi
import com.example.riseandroid.network.auth0.Auth0Api
import com.example.riseandroid.repository.Auth0Repo
import com.example.riseandroid.repository.IAuthRepo
import com.example.riseandroid.repository.MoviePosterRepo
import com.example.riseandroid.repository.MovieRepo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate


interface AppContainer {
    val programRepository: ProgramRepository

    val movieRepo:MovieRepo
    val moviePosterRepo: MoviePosterRepo
    val moviesRepository: MoviesRepository
    val authApiService: Auth0Api
    val ticketRepository: TicketRepository

    val authRepo: IAuthRepo
//    val accountRepository: AccountRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val BASE_URL = "https://dev-viwl48rh7lran3ul.us.auth0.com"
    private val BASE_URL_BACKEND = "https://10.0.2.2:5001/"

    private val riseDatabase = RiseDatabase.getDatabase(context)
    private val movieDao = riseDatabase.movieDao()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val httpClient: OkHttpClient = SslHelper.createOkHttpClient(context, loggingInterceptor)


    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .baseUrl(BASE_URL)
        .build()


    override val moviePosterRepo: MoviePosterRepo by lazy {
        MoviePosterRepo(retrofitBackend.create(MoviesApi::class.java))
    }

    private val retrofitService: LumiereApiService by lazy {
        retrofit.create(LumiereApiService::class.java)
    }

    private val retrofitBackend: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .baseUrl(BASE_URL_BACKEND)
        .build()

    private val retrofitServiceBackend: MoviesApi by lazy {
        retrofitBackend.create(MoviesApi::class.java)
    }

    override val movieRepo: MovieRepo by lazy {
        val movieDao = movieDao
        val movieApi = retrofitServiceBackend
        MovieRepo(movieDao, movieApi)
    }

    override val moviesRepository: MoviesRepository by lazy {
        NetworkMoviesRepository(retrofitService)
    }
    override val programRepository: ProgramRepository by lazy {
        NetworkProgramRepository(retrofitService)
    }
    override val ticketRepository: TicketRepository by lazy {
        NetworkTicketRepository(retrofitService)
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