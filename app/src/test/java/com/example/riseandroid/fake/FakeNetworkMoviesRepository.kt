package com.example.riseandroid.fake

import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.model.Movie

class FakeNetworkMoviesRepository : MoviesRepository {
    override suspend fun getRecentMovies(): List<Movie> {
        return FakeDataSource.LoadRecentMoviesMock()
    }

    override suspend fun getNonRecentMovies(): List<Movie> {
        return FakeDataSource.LoadNonRecenMoviesMock()
    }
}