package com.example.riseandroid.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface IMovieRepo {
    suspend fun getAllMoviesList(selectedDate: String, selectedCinemas: List<String>): Flow<List<MovieModel>>
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
            .onStart {
                withContext(Dispatchers.IO) {
                    refreshMovies(selectedDate, selectedCinemas)
                }
            }

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

    private suspend fun saveShowtimes(movieId: Int, cinemaId: Int, showtimes: List<String>) {
        val showtimeEntities = showtimes.map { showtime ->
            val dateParts = showtime.substring(0, 10).split("-")
            val showDateFormatted = "${dateParts[0]}-${dateParts[1]}-${dateParts[2]}"

            ShowtimeEntity(
                movieId = movieId,
                cinemaId = cinemaId,
                showTime = showtime.substring(11, 16),
                showDate = showDateFormatted
            )
        }
        movieDao.insertShowtimes(showtimeEntities)
    }

}
