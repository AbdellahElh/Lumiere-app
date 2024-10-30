package com.example.riseandroid

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.riseandroid.model.Movie
import com.example.riseandroid.ui.screens.watchlist.WatchlistScreen
import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class WatchlistScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displays_empty_watchlist_message_when_no_movies_are_in_watchlist() {
        // Arrange
        val viewModel = WatchlistViewModel()
        composeTestRule.setContent {
            WatchlistScreen(
                viewModel = viewModel,
                allMovies = emptyList(), // Geen films in de lijst
                onMovieClick = {},
                navController = rememberNavController()
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Your watchlist is empty.").assertIsDisplayed()
    }

    @Test
    fun displays_watchlist_movies_when_movies_are_in_watchlist(): Unit = runBlocking {
        // Arrange
        val viewModel = WatchlistViewModel()
        val movie = Movie(
            movieId = 1L,
            title = "Test Movie",
            posterResourceId = R.drawable.test_movie_poster, // Zorg dat dit een geldige resource is
            description = "This is a test description.",
            genre = "Action",
            length = "120 min",
            director = "John Doe"
        )

        // Voeg de film toe aan de watchlist
        viewModel.toggleMovieInWatchlist(movie.movieId)

        composeTestRule.setContent {
            WatchlistScreen(
                viewModel = viewModel,
                allMovies = listOf(movie), // Bevat één film
                onMovieClick = {},
                navController = rememberNavController()
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Test Movie").assertIsDisplayed()
    }

    @Test
    fun clicking_on_a_movie_item_calls_onMovieClick() {
        // Arrange
        val viewModel = WatchlistViewModel()
        val movie = Movie(
            movieId = 1L,
            title = "Test Movie",
            posterResourceId = R.drawable.test_movie_poster, // Zorg dat dit een geldige resource is
            description = "This is a test description.",
            genre = "Action",
            length = "120 min",
            director = "John Doe"
        )

        // Voeg de film toe aan de watchlist
        viewModel.toggleMovieInWatchlist(movie.movieId)

        var clickedMovieId: Long? = null
        composeTestRule.setContent {
            WatchlistScreen(
                viewModel = viewModel,
                allMovies = listOf(movie),
                onMovieClick = { clickedMovieId = it },
                navController = rememberNavController()
            )
        }

        // Act
        composeTestRule.onNodeWithText("Test Movie").performClick()

        // Assert
        assert(clickedMovieId == movie.movieId)
    }
}

