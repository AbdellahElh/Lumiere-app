package com.example.riseandroid.data.lumiere

import com.example.riseandroid.data.Datasource
import com.example.riseandroid.model.Movie
import com.example.riseandroid.network.LumiereApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface MoviesRepository {
    suspend fun getRecentMovies() : Flow<List<Movie>>
    suspend fun getNonRecentMovies() : Flow<List<Movie>>
}

class NetworkMoviesRepository(private val lumiereApiService: LumiereApiService) : MoviesRepository {
    override suspend fun getRecentMovies(): Flow<List<Movie>> {
        val movies = Datasource().LoadRecentMovies()
        return listOf(movies).asFlow()
    }

    override suspend fun getNonRecentMovies(): Flow<List<Movie>> {
        val movies = Datasource().LoadNonRecentMovies()
        return listOf(movies).asFlow()
    }

}