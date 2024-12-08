package com.example.riseandroid.fake

import com.example.riseandroid.network.ResponseMovie
import com.example.riseandroid.network.WatchlistApi

class FakeWatchlistApi : WatchlistApi {
    private val backendMovies = mutableListOf<ResponseMovie>()

    override suspend fun getWatchlist(): List<ResponseMovie> {
        return backendMovies.toList()
    }

    override suspend fun addToWatchlist(movie: ResponseMovie) {
        if (!backendMovies.any { it.id == movie.id }) {
            backendMovies.add(movie)
        }
    }

    override suspend fun removeFromWatchlist(movieId: Int) {
        backendMovies.removeAll { it.id == movieId }
    }
}
