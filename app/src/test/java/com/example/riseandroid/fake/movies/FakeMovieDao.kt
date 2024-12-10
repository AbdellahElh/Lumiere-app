package com.example.riseandroid.fake.movies

import com.example.riseandroid.data.entitys.CinemaEntity
import com.example.riseandroid.data.entitys.MovieDao
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.ShowtimeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMovieDao:MovieDao {
    override suspend fun insertMovies(movies: List<MovieEntity>) {

    }

    override suspend fun insertMovie(movie: MovieEntity) {

    }

    override suspend fun deleteMovie(movie: MovieEntity) {

    }

    override suspend fun insertCinema(cinemas: CinemaEntity) {

    }

    override suspend fun insertShowtimes(showtimes: List<ShowtimeEntity>) {

    }

    override suspend fun deleteShowtimesForMovie(movieId: Int) {

    }

    override fun getAllMovies(): Flow<List<MovieEntity>> {
        return flowOf(movies)
    }

    override suspend fun getCinemaByName(name: String): CinemaEntity? {
        return cinemas.find { it.name == name }
    }

    override fun getFilteredMoviesByCinemaAndDate(
        selectedDate: String,
        selectedCinemas: List<String>,
        searchTitle: String?
    ): Flow<List<MovieEntity>> {
        return flowOf(movies)
    }

    override suspend fun getMovieById(movieId: Int): MovieEntity? {
        return movies.find { it.id == movieId }
    }

    private val movies = mutableListOf(
        MovieEntity(
            id = 1,
            eventId = 101,
            title = "Fake Movie1",
            genre = "Drama",
            description = "Test Description",
            duration = 100,
            director = "Director Name",
            videoPlaceholderUrl = "video_placeholder_url",
            coverImageUrl = "cover_url",
            bannerImageUrl = "banner_url",
            posterImageUrl = "poster_url",
            movieLink = "movie_link",
            releaseDate = "2021-12-12"
        ),
        MovieEntity(
            id = 2,
            eventId = 102,
            title = "Another Movie",
            genre = "Comedy",
            description = "Another Description",
            duration = 90,
            director = "Another Director",
            videoPlaceholderUrl = "another_placeholder_url",
            coverImageUrl = "another_cover_url",
            bannerImageUrl = "another_banner_url",
            posterImageUrl = "another_poster_url",
            movieLink = "another_movie_link",
            releaseDate = "2021-12-13"
        )
    )
    private val cinemas = mutableListOf(
        CinemaEntity(1, "Cinema 1"),
        CinemaEntity(2, "Cinema 2")
    )
}