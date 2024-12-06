package com.example.riseandroid.mockdata

import com.example.riseandroid.R
import com.example.riseandroid.model.Movie
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.model.Program

class MovieListMock {

    fun LoadRecentMoviesMock() : List<MoviePoster> {
        return listOf<MoviePoster>(
            MoviePoster(id = 1, cover = "fakeCover", releaseDate = "fakeDate"),
            MoviePoster(id = 2, cover = "fakeCover", releaseDate = "fakeDate"))
    }

    fun LoadAllMoviesMock(): List<MovieModel> {
        return listOf(MovieModel(
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
            movieLink = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg"
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
                movieLink = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg"
                eventId = 2
            ))

    }
}