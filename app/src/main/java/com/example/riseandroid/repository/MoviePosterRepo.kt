// MoviePosterRepo.kt
package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.MoviePosterDao
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.network.MoviesApi
import com.example.riseandroid.util.asEntity
import com.example.riseandroid.util.asExternalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

interface IMoviePosterRepo {
    suspend fun getMoviePosters(): Flow<List<MoviePoster>>
}

class MoviePosterRepo(
    private val moviesApi: MoviesApi,
    private val moviePosterDao: MoviePosterDao
) : IMoviePosterRepo {

    override suspend fun getMoviePosters(): Flow<List<MoviePoster>> {
        return moviePosterDao.getAllMoviePosters()
            .map { entities -> entities.map { it.asExternalModel() } }
            .onStart {
                withContext(Dispatchers.IO) {
                    refreshMoviePosters()
                }
            }
    }

    private suspend fun refreshMoviePosters() {
        try {
            val postersFromApi = moviesApi.getMoviePosters()
            val posterEntities = postersFromApi.map { it.asEntity() }
            moviePosterDao.insertMoviePosters(posterEntities)
            Log.d("MoviePosterRepo", "Movie posters refreshed from API")
        } catch (e: Exception) {
            Log.e("MoviePosterRepo", "Error refreshing movie posters: ${e.message}")
        }
    }
}
