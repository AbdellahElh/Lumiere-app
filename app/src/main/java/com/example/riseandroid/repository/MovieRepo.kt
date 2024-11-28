package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.CinemaEntity
import com.example.riseandroid.data.entitys.MovieDao
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.ShowtimeEntity
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.network.MoviesApi
import com.example.riseandroid.util.asEntity
import com.example.riseandroid.util.asExternalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

interface IMovieRepo {
    suspend fun getAllMoviesList(selectedDate: String, selectedCinemas: List<String>): Flow<List<MovieModel>>
    suspend fun getSpecificMovie(movieId: Int): MovieModel
}

class MovieRepo(
    private val movieDao: MovieDao,
    private val movieApi: MoviesApi
):IMovieRepo {

    override suspend fun getAllMoviesList(
        selectedDate: String,
        selectedCinemas: List<String>
    ): Flow<List<MovieModel>> {
        return movieDao.getFilteredMoviesByCinemaAndDate(selectedDate, selectedCinemas)
            .map { entities -> entities.map(MovieEntity::asExternalModel) }
            .onEach { movies ->
                movies.forEach { movie ->
                    Log.d("MoviesList", movie.toString())
                }}
            .onStart {
                withContext(Dispatchers.IO) {
                    refreshMovies(selectedDate, selectedCinemas)
                }
            }

    }

    override suspend fun getSpecificMovie(movieId: Int): MovieModel {
        return movieApi.getMovieById(movieId.toInt())
    }

    suspend fun refreshMovies(selectedDate: String, selectedCinemas: List<String>) {
        try {
            val moviesFromApi = movieApi.getAllMovies(date = selectedDate, cinemas = selectedCinemas)
            val moviesAsEntities = moviesFromApi.map { it.asEntity() }
            movieDao.insertMovies(moviesAsEntities)
            for (movie in moviesFromApi) {
                saveCinemasAndShowtimes(movie)
            }
        } catch (e: Exception) {
            Log.e("MovieRepo", "Error refreshing movies")
        }
    }

    private suspend fun saveCinemasAndShowtimes(movie: MovieModel) {

        for (cinema in movie.cinemas) {
            val newCinema = CinemaEntity(
                id = cinema.id,
                name = cinema.name
            )
            movieDao.insertCinema(newCinema)
            saveShowtimes(movie.id, cinema.id, cinema.showtimes)
        }
    }

    // MovieRepo.kt

    private suspend fun saveShowtimes(movieId: Int, cinemaId: Int, showtimes: List<String>) {
        val showtimeEntities = showtimes.map { showtime ->
            val dateTimeParts = showtime.split("T")
            val showDate = dateTimeParts[0]
            val showTime = dateTimeParts[1].substring(0, 5) // Get HH:mm

            ShowtimeEntity(
                cinemaId = cinemaId,
                movieId = movieId,
                eventId = null,
                showTime = showTime,
                showDate = showDate
            )
        }
        movieDao.insertShowtimes(showtimeEntities)
    }


}
