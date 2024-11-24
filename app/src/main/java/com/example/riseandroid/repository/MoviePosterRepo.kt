package com.example.riseandroid.repository

import com.example.riseandroid.model.MoviePoster
import kotlinx.coroutines.flow.Flow
import android.util.Log
import com.example.riseandroid.network.MoviesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

interface IMoviePosterRepo {
    suspend fun getMoviePosters(): Flow<List<MoviePoster>>
}


class MoviePosterRepo(private val moviesApi: MoviesApi) : IMoviePosterRepo {

    override suspend fun getMoviePosters(): Flow<List<MoviePoster>> = flow {
        val posters = moviesApi.getMoviePosters()
        emit(posters)
    }.onStart {
        // Optionally, handle loading state or caching
        Log.d("MoviePosterRepo", "Fetching movie posters from API")
    }
}
