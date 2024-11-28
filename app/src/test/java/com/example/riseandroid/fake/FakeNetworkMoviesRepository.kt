package com.example.riseandroid.fake

import com.example.riseandroid.data.Datasource
import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.util.concurrent.CountDownLatch

class FakeNetworkMoviesRepository(
) : MoviesRepository {

    override suspend fun getRecentMovies(): Flow<List<Movie>> {
        val movies = FakeDataSource.LoadRecentMoviesMock()
        return listOf(movies).asFlow()
    }

    override suspend fun getNonRecentMovies(): Flow<List<Movie>> {
        val movies = FakeDataSource.LoadNonRecenMoviesMock()
        return listOf(movies).asFlow()
    }

    override suspend fun getSpecificMovie(movieId: Long): Movie? {
        val movies = FakeDataSource.LoadRecentMoviesMock() + FakeDataSource.LoadNonRecenMoviesMock()
        return movies.find { it.movieId == movieId }
    }

    override suspend fun getAllMovies(): Flow<List<Movie>> {
        val movies = FakeDataSource.LoadRecentMoviesMock() + FakeDataSource.LoadNonRecenMoviesMock()
        return listOf(movies).asFlow()
    }
}