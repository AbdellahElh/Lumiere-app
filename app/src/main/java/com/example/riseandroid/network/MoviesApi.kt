package com.example.riseandroid.network

import com.example.riseandroid.model.Cinema
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
    ):  List<ResponseMovie>

    @GET("api/Movie/future-posters")
    suspend fun getMoviePosters(): List<MoviePoster>

    @GET("api/Movie/{id}")
    suspend fun getMovieById(@Path("id") movieId: Int): MovieModel
}


data class ResponseMovie(
    val id: Int,
    val eventId:Int,
    val title: String,
    val coverImageUrl: String?,
    val genre: String?,
    val duration: String?,
    val director: String?,
    val description: String?,
    val video: String?,
    val videoPlaceholderUrl: String?,
    val cast: List<String> = emptyList(),
    val cinemas: List<ResponseCinema> = emptyList()
)

data class ResponseCinema(
    val id:Int,
    val name: String,
    val showtimes: List<String> = emptyList()
)