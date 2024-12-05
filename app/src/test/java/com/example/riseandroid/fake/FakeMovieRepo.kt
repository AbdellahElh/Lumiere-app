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

        return flowOf(movies)
    }

    override suspend fun getMovieById(id: Int): MovieModel {

        return movies.first { it.id == id }
    }
}
