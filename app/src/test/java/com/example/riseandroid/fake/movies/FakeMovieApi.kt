package com.example.riseandroid.fake.movies

import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.network.MovieApiResponseById
import com.example.riseandroid.network.MoviesApi
import com.example.riseandroid.network.ResponseMovie

class FakeMovieApi : MoviesApi {

    override suspend fun getAllMovies(
        date: String,
        cinemas: List<String>,
        title: String?
    ): List<ResponseMovie> {
        return allMoviesResponse
    }

    override suspend fun getMoviePosters(): List<MoviePoster> {
        return moviePostersResponse
    }

    override suspend fun getMovieById(movieId: Int): ResponseMovie {
        return movieResponse;
    }

    private var movieResponse: ResponseMovie = ResponseMovie(
        id = 1,
        title = "Fake Movie1",
        cinemas = emptyList(),
        cast = emptyList(),
        coverImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
        genre = "",
        duration = 100,
        director = "",
        description = "",
        videoPlaceholderUrl = "",
        releaseDate = "12-12-2021",
        bannerImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
        posterImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
        movieLink = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
        eventId = 1
    )

    private val allMoviesResponse: List<ResponseMovie> = listOf(
        ResponseMovie(
            id = 1,
            title = "Fake Movie1",
            cinemas = emptyList(),
            cast = emptyList(),
            coverImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
            genre = "",
            duration = 100,
            director = "",
            description = "",
            videoPlaceholderUrl = "",
            releaseDate = "12-12-2021",
            bannerImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            posterImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            movieLink = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            eventId = 1
        ),
        ResponseMovie(
            id = 1,
            title = "Another Movie",
            cinemas = emptyList(),
            cast = emptyList(),
            coverImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
            genre = "",
            duration = 100,
            director = "",
            description = "",
            videoPlaceholderUrl = "",
            releaseDate = "12-12-2021",
            bannerImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            posterImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            movieLink = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
            eventId = 1
        )
    )
    private val moviePostersResponse: List<MoviePoster> = listOf(
        MoviePoster(
            id =1,
            cover ="cover1",
            releaseDate ="date1"
        ),
        MoviePoster(
            id =2,
            cover ="cover2",
            releaseDate ="date2"
        )
    )



}