package com.example.riseandroid.fake

import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.network.ResponseMovie
import com.example.riseandroid.repository.IMovieRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMovieRepo(private val movies: List<ResponseMovie>) : IMovieRepo {

    override suspend fun getAllMoviesList(
        selectedDate: String,
        selectedCinemas: List<String>,
        searchTitle: String?
    ): Flow<List<ResponseMovie>> {

        return flowOf(movies)
    }

    override suspend fun getMovieById(id: Int): ResponseMovie {

        return movies.first { it.id == id }
    }

    override suspend fun insertMovie(MovieEntity: MovieEntity) {

    }
}
