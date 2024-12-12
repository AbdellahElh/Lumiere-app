package com.example.riseandroid.mockdata

import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.network.ResponseMovie

class MovieListMock {

    fun LoadRecentMoviesMock(): List<MoviePoster> {
        return listOf(
            MoviePoster(id = 1, cover = "fakeCover", releaseDate = "fakeDate"),
            MoviePoster(id = 2, cover = "fakeCover", releaseDate = "fakeDate")
        )
    }

    fun LoadAllMoviesMock(): List<MovieModel> {
        return listOf(
            MovieModel(
                id = 1,
                title = "FakeMovie1",
                cinemas = emptyList(),
                cast = emptyList(),
                coverImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
                genre = "",
                duration = 100,
                director = "",
                description = "",
                videoPlaceholderUrl = "",
                releaseDate = "12-12-2021",
                bannerImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
                posterImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
                movieLink = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
                eventId = 1
            ),
            MovieModel(
                id = 2,
                title = "FakeMovie2",
                cinemas = emptyList(),
                cast = emptyList(),
                coverImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
                genre = "",
                duration = 100,
                director = "",
                description = "",
                videoPlaceholderUrl = "",
                releaseDate = "12-12-2021",
                bannerImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
                posterImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
                movieLink = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
                eventId = 2
            )
        )
    }

    fun loadMovieEntitiesMock(): List<MovieEntity> {
        return listOf(
            MovieEntity(
                id = 101,
                eventId = 1,
                title = "Test Movie 1",
                genre = "Action",
                description = "A test movie 1",
                duration = 120,
                releaseDate = "2021-01-01",
                director = "Test Director 1",
                videoPlaceholderUrl = "https://example.com/video1",
                coverImageUrl = "https://example.com/cover1",
                bannerImageUrl = "https://example.com/banner1",
                posterImageUrl = "https://example.com/poster1",
                movieLink = "https://example.com/movie1",
            ),
            MovieEntity(
                id = 102,
                eventId = 2,
                title = "Test Movie 2",
                genre = "Comedy",
                description = "A test movie 2",
                duration = 90,
                releaseDate = "2021-02-01",
                director = "Test Director 2",
                videoPlaceholderUrl = "https://example.com/video2",
                coverImageUrl = "https://example.com/cover2",
                bannerImageUrl = "https://example.com/banner2",
                posterImageUrl = "https://example.com/poster2",
                movieLink = "https://example.com/movie2",
            )
        )
    }


    fun loadResponseMoviesMock(): List<ResponseMovie> {
        return listOf(
            ResponseMovie(
                id = 1,
                eventId = 0,
                title = "Test Movie",
                coverImageUrl = "https://example.com/image.jpg",
                genre = "Drama",
                duration = 120,
                director = "John Doe",
                description = "This is a test movie description that exceeds 100 characters to test the 'Lees Meer' functionality.",
                videoPlaceholderUrl = "https://example.com/video.jpg",
                cast = listOf("Actor 1", "Actor 2"),
                cinemas = emptyList(),
                releaseDate = "2024-01-01",
                bannerImageUrl = "https://example.com/banner.jpg",
                posterImageUrl = "https://example.com/poster.jpg",
                movieLink = "https://example.com/movie"
            ),
            ResponseMovie(
                id = 2,
                eventId = 1,
                title = "Another Test Movie",
                coverImageUrl = "https://example.com/image2.jpg",
                genre = "Comedy",
                duration = 90,
                director = "Jane Doe",
                description = "A shorter description for a comedy movie.",
                videoPlaceholderUrl = "https://example.com/video2.jpg",
                cast = listOf("Actor A", "Actor B"),
                cinemas = emptyList(),
                releaseDate = "2023-12-31",
                bannerImageUrl = "https://example.com/banner2.jpg",
                posterImageUrl = "https://example.com/poster2.jpg",
                movieLink = "https://example.com/movie2"
            )
        )
    }
}
