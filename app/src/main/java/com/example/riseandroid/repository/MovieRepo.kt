package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.CinemaEntity
import com.example.riseandroid.data.entitys.MovieDao
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.ShowtimeEntity
import com.example.riseandroid.model.Movie
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.network.MoviesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface IMovieRepo {
    suspend fun getAllMoviesList(selectedDate: String, selectedCinemas: List<String>): Flow<ApiResource<List<MovieModel>>>
    suspend fun getAllMovieListFromLocal():NetworkResult<List<MovieModel>>
}

class MovieRepo(
    private val movieDao: MovieDao,
    private val movieApi: MoviesApi
):IMovieRepo
{
//    override suspend fun getAllMoviesList(selectedDate: String, selectedCinemas: List<String>): Flow<List<MovieModel>> {
//        return flow {
//            try {
//                val response = movieApi.getAllMovies(date = selectedDate, cinemas = selectedCinemas)
//
//                if (response.isEmpty()) {
//                    emit(emptyList())
//                } else {
//                    saveMoviesToLocalDatabase(response.take(3))
//                    emit(response)
//                }
//
//            } catch (e: Exception) {
//                val localMoviesResult = getAllMovieListFromLocal()
//
//                if (localMoviesResult is NetworkResult.Success && localMoviesResult.data.isNotEmpty()) {
//                    NetworkResult.Success(localMoviesResult.data)
//                } else {
//                    NetworkResult.Error(e)
//                }
//            }
//        }
//    }


    override suspend fun getAllMoviesList(selectedDate: String,
                                          selectedCinemas: List<String>):Flow<ApiResource<List<MovieModel>>> {
        return flow {
            emit(ApiResource.Loading())
            try {
                val response = movieApi.getAllMovies(date = selectedDate, cinemas = selectedCinemas)

                if (response.isNotEmpty()) {
                    saveMoviesToLocalDatabase(response.take(3))

                }
                emit(ApiResource.Success(response))
            } catch (e: Exception) {
                Log.e("MovieRepo", "Request failed")
                emit(ApiResource.Error("Request failed"))
            }

        }

    }



    private suspend fun saveMoviesToLocalDatabase(movies: List<MovieModel>) {
        withContext(Dispatchers.IO) {
            val localMovieIds = movieDao.getAllMovies().map { it.id }

            for (movie in movies) {
                if (movie.id !in localMovieIds) {
                    saveMovie(movie)
                    saveCinemasAndShowtimes(movie)
                }
            }
            val apiMovieIds = movies.map { it.id }
            val moviesToRemove = movieDao.getAllMovies().filterNot { it.id in apiMovieIds }
            moviesToRemove.forEach { movie ->
                movieDao.deleteShowtimesForMovie(movie.id)
                movieDao.deleteMovie(movie)
            }
        }
    }

    private suspend fun saveMovie(movie: MovieModel) {
        val movieEntity = MovieEntity(
            id = movie.id,
            name = movie.name,
            genre = movie.genre ?: "",
            description = movie.description ?: "",
            duration = movie.duration ?: "",
            director = movie.director ?: "",
            releaseDate = System.currentTimeMillis(),
            videoPlaceholderUrl = movie.videoPlaceholderUrl,
            coverImageUrl = movie.cover,
            bannerImageUrl = movie.cover,
            posterImageUrl = movie.cover,
            movieLink = movie.video ?: ""
        )

        movieDao.insertMovie(movieEntity)
    }

    private suspend fun saveCinemasAndShowtimes(movie: MovieModel) {
        val cinemas = movie.cinemas.map { cinema ->
            var existingCinema = movieDao.getCinemaByName(cinema.name)
            if (existingCinema == null) {
                val newCinema = CinemaEntity(
                    id=cinema.Id,
                    name = cinema.name,
                    location = cinema.name
                )
                movieDao.insertCinema(newCinema)
                existingCinema = movieDao.getCinemaByName(cinema.name)
            }
            existingCinema
        }

        for (cinema in movie.cinemas) {
            saveShowtimes(movie.id, cinema.Id, cinema.showtimes)
        }
    }

    private suspend fun saveShowtimes(movieId: Int, cinemaId: Int, showtimes: List<String>) {
        val showtimeEntities = showtimes.map { showtime ->
            ShowtimeEntity(
                movieId = movieId,
                cinemaId = cinemaId,
                showTime = showtime.substring(11, 16),
                showDate = showtime.substring(0, 10).split("-").let { "${it[2]}-${it[1]}-${it[0]}" }
            )
        }
        movieDao.insertShowtimes(showtimeEntities)
    }


    override suspend fun getAllMovieListFromLocal(): NetworkResult<List<MovieModel>> {
        return try {
            val movieEntities = movieDao.getAllMovies()

            val movieModels = movieEntities.map { movieEntity ->
                MovieModel(
                    id = movieEntity.id,
                    name = movieEntity.name,
                    cover = movieEntity.coverImageUrl,
                    genre = movieEntity.genre,
                    duration = movieEntity.duration,
                    director = movieEntity.director,
                    description = movieEntity.description,
                    video = movieEntity.movieLink,
                    videoPlaceholderUrl = movieEntity.videoPlaceholderUrl,
                    cast = emptyList(),
                    cinemas = emptyList()
                )
            }
            NetworkResult.Success(movieModels)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}
