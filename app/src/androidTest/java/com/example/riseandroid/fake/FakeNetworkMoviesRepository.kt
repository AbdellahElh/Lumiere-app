package com.example.riseandroid.fake

import com.example.riseandroid.data.Datasource
import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.mockdata.MovieListMock
import com.example.riseandroid.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.util.concurrent.CountDownLatch

class FakeNetworkMoviesRepository(
    private val countDownLatch: CountDownLatch? = null
) : MoviesRepository {
    override suspend fun getRecentMovies(): Flow<List<Movie>> {
        if (countDownLatch != null) {
            countDownLatch.await()
            println("delay test")
        }
        val movies = MovieListMock().LoadRecentMoviesMock()
        return listOf(movies).asFlow()
    }

    override suspend fun getNonRecentMovies(): Flow<List<Movie>> {
        val movies = MovieListMock().LoadNonRecentMoviesMock()
        return listOf(movies).asFlow()
    }
    override suspend fun getSpecificMovie(movieId: Long): Movie? {
        val movie = MovieListMock().LoadNonRecentMoviesMock().find { it.movieId == movieId }
        return movie
    }
}