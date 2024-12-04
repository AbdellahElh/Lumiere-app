package com.example.riseandroid.network

import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("api/Movie")
    suspend fun getAllMovies(
        @Query("date") date: String,
        @Query("cinema") cinemas: List<String>,
        @Query("title") title:String?
    ):  List<MovieModel>

    @GET("api/Movie/posters")
    suspend fun getMoviePosters(): List<MoviePoster>

    @GET("api/Movie/{id}")
    suspend fun getMovieById(@Path("id") movieId: Int): MovieModel
}
