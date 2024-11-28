// File: src/test/java/com/example/riseandroid/WatchlistTest.kt

package com.example.riseandroid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.riseandroid.data.entitys.watchlist.UserManager
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.repository.IWatchlistRepo
import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

@ExperimentalCoroutinesApi
class WatchlistTest {

    // Rule for LiveData
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Test Dispatcher
    private val testDispatcher = UnconfinedTestDispatcher()

    // Mocks and Fakes
    private lateinit var watchlistRepo: IWatchlistRepo
    private lateinit var userManager: UserManager
    private lateinit var watchlistViewModel: WatchlistViewModel

    // Fake Data
    private val fakeMovie = MovieModel(
        id = 1,
        title = "Fake Movie",
        cinemas = emptyList(),
        cast = emptyList(),
        coverImageUrl = "",
        genre = "",
        duration = 100,
        director = "",
        description = "",
        videoPlaceholderUrl = "",
        releaseDate = "12-12-2021",
        bannerImageUrl = "",
        posterImageUrl = "",
        movieLink = ""
    )

    private val fakeMovie2 = MovieModel(
        id = 2,
        title = "Fake Movie 2",
        cinemas = emptyList(),
        cast = emptyList(),
        coverImageUrl = "",
        genre = "",
        duration = 120,
        director = "",
        description = "",
        videoPlaceholderUrl = "",
        releaseDate = "01-01-2022",
        bannerImageUrl = "",
        posterImageUrl = "",
        movieLink = ""
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        watchlistRepo = mockk()
        userManager = UserManager()
        watchlistViewModel = WatchlistViewModel(watchlistRepo, userManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testAddingMovieToWatchlist() = runTest {
        // Given
        val userId = 1
        userManager.setUserId(userId)

        coEvery { watchlistRepo.addToWatchlist(fakeMovie, userId) } just runs
        coEvery { watchlistRepo.getMoviesInWatchlist(userId) } returns flowOf(listOf(fakeMovie))

        // When
        watchlistViewModel.toggleMovieInWatchlist(fakeMovie)

        // Then
        coVerify { watchlistRepo.addToWatchlist(fakeMovie, userId) }

        val watchlist = watchlistViewModel.watchlist.first()
        Assert.assertTrue(watchlist.contains(fakeMovie))
    }

    @Test
    fun testRemovingMovieFromWatchlist() = runTest {
        // Given
        val userId = 1
        userManager.setUserId(userId)

        // Initial state: Movie is in the watchlist
        coEvery { watchlistRepo.getMoviesInWatchlist(userId) } returns flowOf(listOf(fakeMovie))
        coEvery { watchlistRepo.removeFromWatchlist(fakeMovie.id, userId) } just runs

        // Load initial watchlist
        watchlistViewModel.refreshWatchlist()

        // When
        watchlistViewModel.toggleMovieInWatchlist(fakeMovie)

        // Then
        coVerify { watchlistRepo.removeFromWatchlist(fakeMovie.id, userId) }

        val watchlist = watchlistViewModel.watchlist.first()
        Assert.assertFalse(watchlist.contains(fakeMovie))
    }

    @Test
    fun testSynchronizingWatchlistWithBackend() = runTest {
        // Given
        val userId = 1
        userManager.setUserId(userId)

        coEvery { watchlistRepo.syncWatchlistWithBackend(userId) } just runs
        coEvery { watchlistRepo.getMoviesInWatchlist(userId) } returns flowOf(listOf(fakeMovie, fakeMovie2))

        // When
        watchlistViewModel.refreshWatchlist()

        // Then
        coVerify { watchlistRepo.syncWatchlistWithBackend(userId) }

        val watchlist = watchlistViewModel.watchlist.first()
        Assert.assertEquals(2, watchlist.size)
        Assert.assertTrue(watchlist.contains(fakeMovie))
        Assert.assertTrue(watchlist.contains(fakeMovie2))
    }

    @Test
    fun testHandlingErrorWhenRemovingMovieAlreadyDeletedOnBackend() = runTest {
        // Given
        val userId = 1
        userManager.setUserId(userId)

        val httpException = mockk<HttpException> {
            every { code() } returns 404
        }

        coEvery { watchlistRepo.removeFromWatchlist(fakeMovie.id, userId) } throws httpException
        coEvery { watchlistRepo.getMoviesInWatchlist(userId) } returns flowOf(listOf(fakeMovie))

        // Collect events
        val events = mutableListOf<WatchlistViewModel.WatchlistEvent>()
        val job = launch {
            watchlistViewModel.eventFlow.toList(events)
        }

        // When
        watchlistViewModel.toggleMovieInWatchlist(fakeMovie)

        // Then
        Assert.assertTrue(events.any { it is WatchlistViewModel.WatchlistEvent.ShowToast && it.message == "Film werd al verwijderd uit de watchlist" })

        job.cancel()
    }

    @Test
    fun testWatchlistIsEmptyWhenUserIsNotLoggedIn() = runTest {
        // Given
        userManager.clearUserId()

        // When
        val watchlist = watchlistViewModel.watchlist.first()

        // Then
        Assert.assertTrue(watchlist.isEmpty())
    }

    @Test
    fun testAddingMovieWhenUserIsNotLoggedInDoesNothing() = runTest {
        // Given
        userManager.clearUserId()

        // When
        watchlistViewModel.toggleMovieInWatchlist(fakeMovie)

        // Then
        coVerify(exactly = 0) { watchlistRepo.addToWatchlist(any(), any()) }
        val watchlist = watchlistViewModel.watchlist.first()
        Assert.assertTrue(watchlist.isEmpty())
    }

    @Test
    fun testHandlingNetworkErrorDuringSynchronization() = runTest {
        // Given
        val userId = 1
        userManager.setUserId(userId)

        coEvery { watchlistRepo.syncWatchlistWithBackend(userId) } throws IOException("Network Error")
        coEvery { watchlistRepo.getMoviesInWatchlist(userId) } returns flowOf(emptyList())

        // Collect events
        val events = mutableListOf<WatchlistViewModel.WatchlistEvent>()
        val job = launch {
            watchlistViewModel.eventFlow.toList(events)
        }

        // When
        watchlistViewModel.refreshWatchlist()

        // Then
        coVerify { watchlistRepo.syncWatchlistWithBackend(userId) }
        Assert.assertTrue(events.any { it is WatchlistViewModel.WatchlistEvent.ShowToast && it.message.contains("Fout bij synchroniseren") })

        job.cancel()
    }

    @Test
    fun testIsInWatchlistReturnsCorrectValue() = runTest {
        // Given
        val userId = 1
        userManager.setUserId(userId)

        coEvery { watchlistRepo.getMoviesInWatchlist(userId) } returns flowOf(listOf(fakeMovie))

        // When
        watchlistViewModel.refreshWatchlist()
        val isInWatchlist = watchlistViewModel.isInWatchlist(fakeMovie.id)
        val isNotInWatchlist = watchlistViewModel.isInWatchlist(fakeMovie2.id)

        // Then
        Assert.assertTrue(isInWatchlist)
        Assert.assertFalse(isNotInWatchlist)
    }
}
