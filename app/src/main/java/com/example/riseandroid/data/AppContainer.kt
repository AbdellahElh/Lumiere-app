package com.example.riseandroid.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.example.riseandroid.data.entitys.watchlist.UserManager
import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.data.lumiere.NetworkMoviesRepository
import com.example.riseandroid.data.lumiere.NetworkProgramRepository
import com.example.riseandroid.data.lumiere.NetworkTicketRepository
import com.example.riseandroid.data.lumiere.ProgramRepository
import com.example.riseandroid.data.lumiere.TicketRepository
import com.example.riseandroid.network.LumiereApiService
import com.example.riseandroid.network.EventsApi
import com.example.riseandroid.network.MoviesApi
import com.example.riseandroid.network.WatchlistApi
import com.example.riseandroid.network.auth0.Auth0Api
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.Auth0Repo
import com.example.riseandroid.repository.EventRepo
import com.example.riseandroid.repository.IAuthRepo
import com.example.riseandroid.repository.IWatchlistRepo
import com.example.riseandroid.repository.MovieRepo
import com.example.riseandroid.repository.WatchlistRepo
import com.example.riseandroid.ui.screens.account.AuthState
import com.example.riseandroid.ui.screens.account.AuthViewModel
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val programRepository: ProgramRepository

    val movieRepo:MovieRepo

    val moviesRepository: MoviesRepository
    val authApiService: Auth0Api
    val ticketRepository: TicketRepository
    val authRepo: IAuthRepo
    val movieRepo: MovieRepo
    val watchlistRepo: IWatchlistRepo
    val userId: Int
    val userManager: UserManager
    val authViewModel: AuthViewModel
    val eventRepo: EventRepo
}

class DefaultAppContainer(
    private val context: Context,
    override val userManager: UserManager
) : AppContainer {
    private val BASE_URL = "https://dev-viwl48rh7lran3ul.us.auth0.com"
    private val BASE_URL_BACKEND = "https://10.0.2.2:5001"

    private val database = Room.databaseBuilder(
        context.applicationContext,
        RiseDatabase::class.java,
        "rise_database"
    ).build()

    private val riseDatabase = database
    private val movieDao = riseDatabase.movieDao()
    private val eventDao = riseDatabase.eventDao()
    private val watchlistDao = riseDatabase.watchlistDao()

    private val auth0 = Auth0(context)

    private val authenticationApiClient = AuthenticationAPIClient(auth0)

    private val credentialsManager = CredentialsManager(
        authenticationApiClient,
        SharedPreferencesStorage(context)
    )

    private val authApi: Auth0Api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Auth0Api::class.java)

    override val authApiService: Auth0Api = authApi

    override val authRepo: IAuthRepo = Auth0Repo(
        authentication = authenticationApiClient,
        credentialsManager = credentialsManager,
        authApi = authApi,
        auth0 = auth0
    )

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient: OkHttpClient = SslHelper.createOkHttpClient(context, loggingInterceptor)
        .newBuilder()
        .addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            val credentials = runBlocking {
                when (val result = authRepo.getCredentials().last()) {
                    is ApiResource.Success -> result.data
                    else -> null
                }
            }

            if (credentials?.accessToken.isNullOrEmpty()) {
                Log.w("HttpClient", "Geen geldig token beschikbaar. Verzoek zonder token.")
            } else {
                Log.d("HttpClient", "Token gevonden: ${credentials?.accessToken}")
                requestBuilder.addHeader("Authorization", "Bearer ${credentials?.accessToken}")
            }

            chain.proceed(requestBuilder.build())
        }
        .build()

    private val retrofitBackend: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .baseUrl("$BASE_URL_BACKEND/")
        .build()

    private val retrofitServiceBackend: MoviesApi by lazy {
        retrofitBackend.create(MoviesApi::class.java)
    }

    override val movieRepo: MovieRepo by lazy {
        MovieRepo(movieDao, retrofitServiceBackend)
    }

    private val watchlistApi: WatchlistApi = Retrofit.Builder()
        .baseUrl("$BASE_URL_BACKEND/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(WatchlistApi::class.java)

    override val watchlistRepo: IWatchlistRepo by lazy {
        WatchlistRepo(watchlistDao, watchlistApi)
    }

    private val eventsApi: EventsApi by lazy {
        retrofitBackend.create(EventsApi::class.java)
    }

    override val eventRepo: EventRepo by lazy {
        EventRepo(eventDao, eventsApi)
    }

    override val moviesRepository: MoviesRepository by lazy {
        NetworkMoviesRepository(retrofitBackend.create(LumiereApiService::class.java))
    }

    override val programRepository: ProgramRepository by lazy {
        NetworkProgramRepository(retrofitBackend.create(LumiereApiService::class.java))
    }

    override val ticketRepository: TicketRepository by lazy {
        NetworkTicketRepository(retrofitBackend.create(LumiereApiService::class.java))
    }


    override val authViewModel: AuthViewModel = AuthViewModel(
        authRepo = authRepo,
        watchlistRepo = watchlistRepo,
        userManager = userManager
    )

    override val userId: Int
        get() {
            val state = authViewModel.authState.value
            return if (state is AuthState.Authenticated) {
                val idToken = state.credentials.idToken
                idToken?.let { token ->
                    try {
                        val payload = decodeJWT(token)
                        val authId = payload?.getString("sub")
                        val userId = authId?.split("|")?.getOrNull(1)?.toIntOrNull() ?: 0
                        Log.d("DefaultAppContainer", "userId resolved: $userId")
                        userId
                    } catch (e: Exception) {
                        Log.e("DefaultAppContainer", "Error parsing userId from token", e)
                        0
                    }
                } ?: run {
                    Log.w("DefaultAppContainer", "idToken is null")
                    0
                }
            } else {
                Log.d("DefaultAppContainer", "User not authenticated, defaulting userId to 0")
                0
            }
        }

    private fun decodeJWT(jwt: String): JSONObject? {
        return try {
            val parts = jwt.split(".")
            if (parts.size < 2) return null
            val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
            JSONObject(payload)
        } catch (e: Exception) {
            null
        }
    }
}

