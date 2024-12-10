package com.example.riseandroid.fake

import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.network.ResponseMovie
import com.example.riseandroid.repository.IWatchlistRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

open class FakeWatchlistRepo : IWatchlistRepo {

    private val fakeMovies = mutableListOf(
        MovieModel(
            id = 1,
            title = "Fake Watchlist Movie 1",
            cinemas = emptyList(),
            cast = emptyList(),
            coverImageUrl = "",
            genre = "",
            duration = 120,
            director = "",
            description = "",
            videoPlaceholderUrl = "",
            releaseDate = "",
            bannerImageUrl = "",
            posterImageUrl = "",
            movieLink = "",
            eventId = 1
        ),
        MovieModel(
            id = 2,
            title = "Fake Watchlist Movie 2",
            cinemas = emptyList(),
            cast = emptyList(),
            coverImageUrl = "",
            genre = "",
            duration = 100,
            director = "",
            description = "",
            videoPlaceholderUrl = "",
            releaseDate = "",
            bannerImageUrl = "",
            posterImageUrl = "",
            movieLink = "",
            eventId = 2
        )
    )

    private val fakeMoviesResponse = mutableListOf(
        ResponseMovie(
            id = 1,
            title = "Fake Watchlist Movie 1",
            cinemas = emptyList(),
            cast = emptyList(),
            coverImageUrl = "",
            genre = "",
            duration = 120,
            director = "",
            description = "",
            videoPlaceholderUrl = "",
            releaseDate = "",
            bannerImageUrl = "",
            posterImageUrl = "",
            movieLink = "",
            eventId = 1
        ),
        ResponseMovie(
            id = 2,
            title = "Fake Watchlist Movie 2",
            cinemas = emptyList(),
            cast = emptyList(),
            coverImageUrl = "",
            genre = "",
            duration = 100,
            director = "",
            description = "",
            videoPlaceholderUrl = "",
            releaseDate = "",
            bannerImageUrl = "",
            posterImageUrl = "",
            movieLink = "",
            eventId = 2
        )
    )

    override fun getMoviesInWatchlist(userId: Int): Flow<List<MovieModel>> {
        return flowOf(fakeMovies)
    }

    override suspend fun addToWatchlist(movie: ResponseMovie, userId: Int) {
        fakeMoviesResponse.add(movie)
    }

    override suspend fun removeFromWatchlist(movieId: Int, userId: Int) {
        fakeMovies.removeAll { it.id == movieId }
    }

    override suspend fun getWatchlistId(userId: Int): Int {
        return 1 // Return a dummy watchlist ID
    }

    override suspend fun syncWatchlistWithBackend(userId: Int) {
        // No-op for fake repo
    }
}

