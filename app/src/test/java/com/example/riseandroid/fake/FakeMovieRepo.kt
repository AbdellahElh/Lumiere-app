package com.example.riseandroid.fake

import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.repository.IMovieRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMovieRepo(private val movies: List<MovieModel>) : IMovieRepo {
    override suspend fun getAllMoviesList(date: String, cinemas: List<String>): Flow<List<MovieModel>> {
        return flowOf(movies)
    }

    // Implement other methods if needed
}
