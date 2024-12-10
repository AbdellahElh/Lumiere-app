// File: src/test/java/com/example/riseandroid/WatchlistTest.kt

package com.example.riseandroid

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.riseandroid.fake.FakeUserManager
import com.example.riseandroid.fake.FakeWatchlistRepo
import com.example.riseandroid.fake.FakeWatchlistViewModel
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.network.ResponseMovie
import com.example.riseandroid.repository.IWatchlistRepo
import com.example.riseandroid.ui.screens.watchlist.WatchlistScreen
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WatchlistUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val fakeRepo = FakeWatchlistRepo()
    private val fakeUserManager = FakeUserManager()

    @Test
    fun watchlistScreen_DisplaysMovies_WhenWatchlistIsNotEmpty() {
        val viewModel = FakeWatchlistViewModel(fakeRepo, fakeUserManager, context)

        composeTestRule.setContent {
            val navController = rememberNavController()
            WatchlistScreen(
                onMovieClick = {},
                navController = navController,
                watchlistViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Fake Watchlist Movie 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Fake Watchlist Movie 2").assertIsDisplayed()
    }

    @Test
    fun watchlistScreen_DisplaysEmptyMessage_WhenWatchlistIsEmpty() {
        val emptyRepo = object : IWatchlistRepo {
            override fun getMoviesInWatchlist(userId: Int): Flow<List<MovieModel>> {
                return flowOf(emptyList())
            }
            override suspend fun addToWatchlist(movie: ResponseMovie, userId: Int) {}
            override suspend fun removeFromWatchlist(movieId: Int, userId: Int) {}
            override suspend fun getWatchlistId(userId: Int): Int = 1
            override suspend fun syncWatchlistWithBackend(userId: Int) {}
        }

        val viewModel = FakeWatchlistViewModel(emptyRepo, fakeUserManager, context)

        composeTestRule.setContent {
            val navController = rememberNavController()
            WatchlistScreen(
                onMovieClick = {},
                navController = navController,
                watchlistViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Uw watchlist is leeg.").assertIsDisplayed()
    }

    @Test
    fun watchlistScreen_NavigatesToMovieDetail_OnMovieClick() {
        var clickedMovieId: Int? = null
        val viewModel = FakeWatchlistViewModel(fakeRepo, fakeUserManager, context)

        composeTestRule.setContent {
            val navController = rememberNavController()
            WatchlistScreen(
                onMovieClick = { movieId ->
                    clickedMovieId = movieId
                },
                navController = navController,
                watchlistViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Fake Watchlist Movie 1").performClick()

        assertEquals(1, clickedMovieId)
    }
}


