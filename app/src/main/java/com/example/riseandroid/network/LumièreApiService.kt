package com.example.riseandroid.network

import com.example.riseandroid.model.Movie
import retrofit2.http.GET

private const val BASE_URL =
    "hier komt de base url van de api"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface LumièreApiService {
    @GET("moviesEndpoint")
    suspend fun getMovies() : List<Movie>
}

object LumièreApi {
    val retrofitService: LumièreApiService by lazy {
        retrofit.create(LumièreApiService::class.java)
    }
}