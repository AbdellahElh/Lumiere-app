package com.example.riseandroid.data.lumière

import com.example.riseandroid.model.Movie
import com.example.riseandroid.network.LumièreApi

interface MoviesRepository {
    suspend fun getRecentMovies() : List<Movie>
    suspend fun getNonRecentMovies() : List<Movie>
}

class NetworkMoviesRepository() : MoviesRepository {
    override suspend fun getRecentMovies(): List<Movie> {
        return LumièreApi.retrofitService.getMovies()
    }

    override suspend fun getNonRecentMovies(): List<Movie> {
        TODO("Not yet implemented")
    }

}