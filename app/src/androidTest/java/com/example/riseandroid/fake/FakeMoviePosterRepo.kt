package com.example.riseandroid.fake
import com.example.riseandroid.mockdata.MovieListMock
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.repository.IMoviePosterRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class FakeMoviePosterRepo : IMoviePosterRepo {
    private val movieListMock = MovieListMock()
    override suspend fun refreshMoviePosters() {
        // No operation needed for fake repository
    }
    override suspend fun getMoviePosters(): Flow<List<MoviePoster>> {
        return flow {
            emit(movieListMock.LoadRecentMoviesMock())
        }
    }
    // Implement other methods from IMoviePosterRepo if necessary
}