package com.example.riseandroid.network

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
    ):  List<ResponseMovie>

    @GET("api/Movie/future-posters")
    suspend fun getMoviePosters(): List<MoviePoster>

    @GET("api/Movie/{id}")
    suspend fun getMovieById(@Path("id") movieId: Int): ResponseMovie
}


data class ResponseMovie(
    val id: Int,
    val eventId: Int?,
    val title: String,
    val genre: String,
    val description: String,
    val duration: Int,
    val director: String,
    val cast: List<String> = emptyList(),
    val releaseDate: String,
    val videoPlaceholderUrl: String?,
    val coverImageUrl: String?,
    val bannerImageUrl: String?,
    val posterImageUrl: String?,
    val movieLink: String,
    val cinemas: List<ResponseCinema> = emptyList()
)

data class ResponseCinema(
    val id:Int,
    val name: String,
    val showtimes: List<String> = emptyList()
)