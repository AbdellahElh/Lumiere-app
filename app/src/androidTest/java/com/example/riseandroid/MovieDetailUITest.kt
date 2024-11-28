package com.example.riseandroid

import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.riseandroid.mockdata.MovieListMock
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailScreen
import org.junit.Rule
import org.junit.Test

class MovieDetailUITest {

    @get:Rule
    val movieDetailTestRule = createComposeRule()

    private val movie = MovieListMock().LoadNonRecentMoviesMock().first()

    @Test
    fun movieDetailScreen_Displays_Movie_Details() {
        movieDetailTestRule.setContent {
            val navController = rememberNavController()
            Surface(modifier = Modifier) {
                //    MovieDetailScreen(movieId = movie.movieId, navController = navController)
            }
        }

        movieDetailTestRule.onNodeWithText(movie.movie.title).assertIsDisplayed()

        movieDetailTestRule.onNodeWithText("Directeur: ${movie.movie.director}").assertIsDisplayed()

        movie.movie.genre?.let { movieDetailTestRule.onNodeWithText(it).assertIsDisplayed() }

        movie.movie.length?.let { movieDetailTestRule.onNodeWithText(it).assertIsDisplayed() }

        movieDetailTestRule.onNodeWithText(movie.movie.description.take(100)).assertIsDisplayed()

        movieDetailTestRule.onNodeWithTag("BackButton").assertIsDisplayed()
    }

    @Test
    fun movieDetailScreen_Displays_LeesMeer_If_Description_Too_Long() {
        movieDetailTestRule.setContent {
            val navController = rememberNavController()
            Surface(modifier = Modifier) {
                //   MovieDetailScreen(movieId = movie.movieId, navController = navController)
            }
        }

        if (movie.movie.description.length > 100) {
            movieDetailTestRule.onNodeWithText("Lees Meer").assertIsDisplayed()
        }
    }
}