package com.example.riseandroid.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WatchlistApi {

    @GET("api/Watchlist")
    suspend fun getWatchlist(): List<ResponseMovie>

    @POST("api/Watchlist")
    suspend fun addToWatchlist(@Body movie: ResponseMovie)

    @DELETE("api/Watchlist/{movieId}")
    suspend fun removeFromWatchlist(@Path("movieId") movieId: Int)
}
