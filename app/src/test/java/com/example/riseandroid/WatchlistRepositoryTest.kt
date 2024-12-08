package com.example.riseandroid

import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.watchlist.WatchlistEntity
import com.example.riseandroid.fake.FakeWatchlistApi
import com.example.riseandroid.fake.FakeWatchlistDao
import com.example.riseandroid.network.ResponseMovie
import com.example.riseandroid.repository.IWatchlistRepo
import com.example.riseandroid.repository.WatchlistRepo
import com.example.riseandroid.rules.TestDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WatchlistRepositoryTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val watchlistDao = FakeWatchlistDao()
    private val watchlistApi = FakeWatchlistApi()
    private lateinit var watchlistRepo: IWatchlistRepo

    private val testUserId = 1
    private val testMovie = ResponseMovie(
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
        eventId = 1)
    private val testMovieEntity = MovieEntity(
        id = 1,
        title = "Fake Movie1",
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
        eventId = 1)

    @Before
    fun setUp() = runTest {
        watchlistRepo = WatchlistRepo(watchlistDao, watchlistApi)
        watchlistDao.insertWatchlist(WatchlistEntity(userId = testUserId))
    }

    @Test
    fun `addToWatchlist adds movie to local and backend`() = runTest {
        watchlistDao.addMovieToWatchlist(testMovieEntity)
        watchlistRepo.addToWatchlist(testMovie, testUserId)

        val moviesInWatchlist = watchlistRepo.getMoviesInWatchlist(testUserId).first()
        assertTrue(moviesInWatchlist.any { it.id == testMovie.id })

        val backendMovies = watchlistApi.getWatchlist()
        assertTrue(backendMovies.any { it.id == testMovie.id })
    }


    @Test
    fun `removeFromWatchlist removes movie from local and backend`() = runTest {
        watchlistRepo.addToWatchlist(testMovie, testUserId)

        watchlistRepo.removeFromWatchlist(testMovie.id, testUserId)

        val moviesInWatchlist = watchlistRepo.getMoviesInWatchlist(testUserId).first()
        assertTrue(moviesInWatchlist.none { it.id == testMovie.id })

        val backendMovies = watchlistApi.getWatchlist()
        assertTrue(backendMovies.none { it.id == testMovie.id })
    }

    @Test
    fun `getWatchlistId creates a watchlist if not present`() = runTest {
        val userWithoutWatchlist = 2
        val watchlistId = watchlistRepo.getWatchlistId(userWithoutWatchlist)

        val returnedId = watchlistDao.getWatchlistIdForUser(userWithoutWatchlist)
        assertEquals(watchlistId, returnedId)
    }

    @Test
    fun `syncWatchlistWithBackend clears local and inserts backend data`() = runTest {
        val backendMovie = ResponseMovie(id = 200,
            title = "Backend Movie",
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
            eventId = 1)
        watchlistApi.addToWatchlist(backendMovie)

        watchlistRepo.syncWatchlistWithBackend(testUserId)

        val moviesInWatchlist = watchlistRepo.getMoviesInWatchlist(testUserId).first()
        assertTrue(moviesInWatchlist.any { it.id == backendMovie.id })
    }
}
