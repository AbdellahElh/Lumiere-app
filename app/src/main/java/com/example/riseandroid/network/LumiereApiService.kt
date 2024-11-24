package com.example.riseandroid.network

import com.example.riseandroid.model.MovieModel
import retrofit2.http.GET
import retrofit2.http.Path

interface LumiereApiService {
    @GET("api/Movies")
    suspend fun getMovies(): List<MovieModel>

    @GET("api/Movies/{id}")
    suspend fun getMovieById(@Path("id") id: Int): MovieModel
}
