package com.example.riseandroid.fake

import com.example.riseandroid.data.Datasource
import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.model.Movie
import kotlinx.coroutines.flow.asFlow

class FakeNetworkMoviesRepository : MoviesRepository {
    override suspend fun getRecentMovies(): List<Movie> {
        return Datasource().LoadRecentMovies()
    }

    override suspend fun getNonRecentMovies(): List<Movie> {
        return Datasource().LoadNonRecentMovies()
    }
}