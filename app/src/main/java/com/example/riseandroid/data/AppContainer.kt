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
import com.example.riseandroid.data.lumiere.NetworkTicketRepository
import com.example.riseandroid.data.lumiere.TicketRepository


interface AppContainer {
    val ticketRepository: TicketRepository

    val moviesRepository: MoviesRepository
//    val accountRepository: AccountRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val BASE_URL = "https://10.0.2.2"

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
    override val ticketRepository: TicketRepository by lazy {
        NetworkTicketRepository(retrofitService)
    }

//    override val accountRepository : AccountRepository by lazy {
//        OfflineAccountRepository(Database.getDatabase(context))
//    }

}