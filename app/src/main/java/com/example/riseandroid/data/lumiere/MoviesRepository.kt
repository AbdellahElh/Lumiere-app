package com.example.riseandroid.data.lumiere

import com.example.riseandroid.data.Datasource
import com.example.riseandroid.model.Movie
import com.example.riseandroid.network.LumiereApiService

interface MoviesRepository {
    suspend fun getRecentMovies() : List<Movie>
    suspend fun getNonRecentMovies() : List<Movie>
}

class NetworkMoviesRepository(private val lumiereApiService: LumiereApiService) : MoviesRepository {
    override suspend fun getRecentMovies(): List<Movie> {
        return Datasource().LoadRecentMovies()
    }

    override suspend fun getNonRecentMovies(): List<Movie> {
        return Datasource().LoadRecentMovies()
    }

}