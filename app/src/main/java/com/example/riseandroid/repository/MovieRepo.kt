package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.CinemaEntity
import com.example.riseandroid.data.entitys.MovieDao
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.ShowtimeEntity
import com.example.riseandroid.network.MoviesApi
import com.example.riseandroid.network.ResponseMovie
import com.example.riseandroid.util.asEntity
import com.example.riseandroid.util.asResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

interface IMovieRepo {
    suspend fun getAllMoviesList(selectedDate: String, selectedCinemas: List<String>,searchTitle: String?): Flow<List<ResponseMovie>>
    suspend fun getMovieById(id: Int): ResponseMovie
}

class MovieRepo(
    private val movieDao: MovieDao,
    private val movieApi: MoviesApi
):IMovieRepo {

    override suspend fun getAllMoviesList(
        selectedDate: String,
        selectedCinemas: List<String>,
        searchTitle: String?
    ): Flow<List<ResponseMovie>> {
        val searchTitleWithPercent = if (searchTitle.isNullOrEmpty()) "%" else "%$searchTitle%"

        return movieDao.getFilteredMoviesByCinemaAndDate(selectedDate, selectedCinemas,searchTitleWithPercent)
            .map { entities -> entities.map(MovieEntity::asResponse) }
            .onStart {
                withContext(Dispatchers.IO) {
                    refreshMovies(selectedDate, selectedCinemas, searchTitle)
                }
            }

    }

    override suspend fun getMovieById(id: Int): ResponseMovie {
        val movieEntity = movieDao.getMovieById(id)
        if (movieEntity != null) {
            return movieEntity.asResponse()
        }
        try {
            val movieFromApi = movieApi.getMovieById(id)
            movieDao.insertMovie(movieFromApi.asEntity())

            return movieFromApi
        } catch (e: Exception) {
            Log.e("MovieRepo", "Error fetching movie from API: ${e.message}")
            return ResponseMovie(
                id = 0,
                eventId = 0,
                title = "",
                coverImageUrl = "",
                genre = "",
                duration = 0,
                director = "",
                description = "",
                videoPlaceholderUrl = "",
                cast = emptyList(),
                cinemas = emptyList(),
                releaseDate = "",
                bannerImageUrl = "",
                posterImageUrl = "",
                movieLink = ""
            )
        }
    }

    suspend fun refreshMovies(selectedDate: String, selectedCinemas: List<String>,searchTitle:String?) {
        try {
            val search = if (searchTitle.isNullOrEmpty()) null else searchTitle
            val moviesFromApi = movieApi.getAllMovies(
                date = selectedDate,
                cinemas = selectedCinemas,
                title = search
            )
            val moviesAsEntities = moviesFromApi.map { it.asEntity() }
            movieDao.insertMovies(moviesAsEntities)
            for (movie in moviesFromApi) {
                saveCinemasAndShowtimes(movie)
            }
        } catch (e: Exception) {
            Log.e("MovieRepo", "Error refreshing movies")
        }
    }

    private suspend fun saveCinemasAndShowtimes(movie: ResponseMovie) {

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