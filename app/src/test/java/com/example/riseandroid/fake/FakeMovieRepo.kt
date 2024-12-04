package com.example.riseandroid.fake

import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.repository.IMovieRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMovieRepo(private val movies: List<MovieModel>) : IMovieRepo {

    override suspend fun getAllMoviesList(
        selectedDate: String,
        selectedCinemas: List<String>,
        searchTitle: String?
    ): Flow<List<MovieModel>> {
        // Return the list of movies wrapped in a Flow
        return flowOf(movies)
    }

    override suspend fun getMovieById(id: Int): MovieModel {
        // Return a movie by its ID from the provided list
        return movies.first { it.id == id }
    }
}
