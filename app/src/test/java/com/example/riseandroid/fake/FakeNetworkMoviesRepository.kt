package com.example.riseandroid.fake

import com.example.riseandroid.data.Datasource
import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class FakeNetworkMoviesRepository : MoviesRepository {
    override suspend fun getRecentMovies(): Flow<List<Movie>> {
        val movies = Datasource().LoadRecentMovies()
        return listOf(movies).asFlow()
    }

    override suspend fun getNonRecentMovies(): Flow<List<Movie>> {
        val movies = Datasource().LoadNonRecentMovies()
        return listOf(movies).asFlow()
    }
}