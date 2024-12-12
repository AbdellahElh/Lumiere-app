package com.example.riseandroid

import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.riseandroid.fake.FakeAuthViewModel
import com.example.riseandroid.fake.FakeMovieRepo
import com.example.riseandroid.fake.FakeTicketRepository
import com.example.riseandroid.fake.FakeTicketViewModel
import com.example.riseandroid.fake.FakeUserManager
import com.example.riseandroid.fake.FakeWatchlistRepo
import com.example.riseandroid.fake.FakeWatchlistViewModel
import com.example.riseandroid.mockdata.MovieListMock
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailScreen
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.riseandroid.fake.FakeMovieDetailViewModel
import com.example.riseandroid.network.ResponseMovie
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailUiState
import org.junit.Rule
import org.junit.Test

class MovieDetailUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val movie: ResponseMovie = MovieListMock().loadResponseMoviesMock().first()

    @Test
    fun movieDetailScreen_Displays_Movie_Details() {
        // Inject the MovieDetailUiState directly into the test
        composeTestRule.setContent {
            val navController = rememberNavController()

            Surface(modifier = Modifier) {
                // Force the UI state to Success with the mock movie
                MovieDetailScreen(
                    movieId = movie.id,
                    navController = navController,
                    viewModel = FakeMovieDetailViewModel(MovieDetailUiState.Success(movie)),
                    ticketViewModel = FakeTicketViewModel(
                        ticketRepository = FakeTicketRepository()
                    ),
                    watchlistViewModel = FakeWatchlistViewModel(
                        watchlistRepo = FakeWatchlistRepo(),
                        movieRepo = FakeMovieRepo(),
                        userManager = FakeUserManager(),
                        context = LocalContext.current
                    ),
                    authViewModel = FakeAuthViewModel()
                )
            }
        }

        // Perform assertions
        composeTestRule.onNodeWithText(movie.title).assertIsDisplayed()
        composeTestRule.onNodeWithText("Directeur: ${movie.director}").assertIsDisplayed()
        composeTestRule.onNodeWithText(movie.genre).assertIsDisplayed()
        composeTestRule.onNodeWithText("${movie.duration}").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    @Test
    fun movieDetailScreen_Displays_LeesMeer_If_Description_Too_Long() {
        val fakeRepo = FakeMovieRepo()
        val viewModel = MovieDetailViewModel(movieId = movie.id, movieRepo = fakeRepo)

        composeTestRule.setContent {
            val navController = rememberNavController()

            // Use your fake or mock ViewModels
            val ticketViewModel = FakeTicketViewModel(FakeTicketRepository())
            val watchlistViewModel = FakeWatchlistViewModel(
                watchlistRepo = FakeWatchlistRepo(),
                movieRepo = fakeRepo,
                userManager = FakeUserManager(),
                context = LocalContext.current
            )
            val authViewModel = FakeAuthViewModel()

            Surface(modifier = Modifier) {
                MovieDetailScreen(
                    movieId = movie.id,
                    navController = navController,
                    viewModel = viewModel,
                    ticketViewModel = ticketViewModel,
                    watchlistViewModel = watchlistViewModel,
                    authViewModel = authViewModel
                )
            }
        }

        if (movie.description.length > 100) {
            composeTestRule.onNodeWithText("Lees Meer").assertIsDisplayed()
        }
    }
}
