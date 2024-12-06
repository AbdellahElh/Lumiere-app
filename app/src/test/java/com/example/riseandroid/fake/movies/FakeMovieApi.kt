package com.example.riseandroid.fake.movies

import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
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

    override suspend fun getMovieById(movieId: Int): MovieModel {
        return movieResponse;
    }

    private var movieResponse: MovieModel = MovieModel(
        id = 1,
        title = "Test Movie",
        coverImageUrl = "test_url",
        genre = "Drama",
        duration = "120 min",
        director = "Director Name",
        description = "Test Description",
        video = "video_url",
        videoPlaceholderUrl = "placeholder_url",
        cast = emptyList(),
        cinemas = emptyList(),
        eventId = 1
    )

    private val allMoviesResponse: List<ResponseMovie> = listOf(
        ResponseMovie(
            id = 1,
            title = "Test Movie",
            coverImageUrl = "test_url",
            genre = "Drama",
            duration = "120 min",
            director = "Director Name",
            description = "Test Description",
            video = "video_url",
            videoPlaceholderUrl = "placeholder_url",
            cast = emptyList(),
            cinemas = emptyList(),
            eventId = 1
        ),
        ResponseMovie(
            id = 2,
            title = "Another Movie",
            coverImageUrl = "another_test_url",
            genre = "Comedy",
            duration = "90 min",
            director = "Another Director",
            description = "Another test description",
            video = "another_video_url",
            videoPlaceholderUrl = "another_placeholder_url",
            cast = emptyList(),
            cinemas = emptyList(),
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