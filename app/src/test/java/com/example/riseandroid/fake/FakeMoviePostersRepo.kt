package com.example.riseandroid.fake

import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.repository.IMoviePosterRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMoviePosterRepo(private val posters: List<MoviePoster>) : IMoviePosterRepo {
    override suspend fun getMoviePosters(): Flow<List<MoviePoster>> {
        return flowOf(posters)
    }

    override suspend fun refreshMoviePosters() {
        // Do nothing or simulate data refresh if needed
    }
}
