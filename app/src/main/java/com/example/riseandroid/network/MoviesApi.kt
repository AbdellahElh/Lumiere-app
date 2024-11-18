package com.example.riseandroid.network

import com.example.riseandroid.model.MovieModel
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("api/Movie")
    suspend fun getAllMovies(
        @Query("date") date: String,
        @Query("cinema") cinemas: List<String>
    ):  List<MovieModel>

}