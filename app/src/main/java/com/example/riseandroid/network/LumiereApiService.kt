package com.example.riseandroid.network

import com.example.riseandroid.model.Movie
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

interface LumiereApiService {
    @GET("moviesEndpoint")
    suspend fun getMovies(): List<Movie>
}



