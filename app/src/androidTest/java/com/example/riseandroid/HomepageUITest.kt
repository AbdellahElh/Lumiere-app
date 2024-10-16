package com.example.riseandroid

import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.riseandroid.mockdata.MovieListMock
import com.example.riseandroid.ui.screens.homepage.Homepage
import com.example.riseandroid.ui.screens.homepage.ResultScreen
import org.junit.Rule
import org.junit.Test

class HomepageUITest {
    @get:Rule
    val homepageTestRule = createComposeRule()

    val recentMovieList = MovieListMock().LoadRecentMoviesMock()
    val allMovieList = MovieListMock().LoadAllMoviesMock()

    @Test
    fun titles_Placed_On_Homescreen() {
        homepageTestRule.setContent {
            Surface(modifier = Modifier) {
                ResultScreen(recentMovieList, allMovieList)
            }
        }
        homepageTestRule.onNodeWithText("Nieuwe films").assertIsDisplayed()
        homepageTestRule.onNodeWithText("Alle films").assertIsDisplayed()
    }

    @Test
    fun list_Of_Movies_Is_Shown(){
        homepageTestRule.setContent {
            Surface(modifier = Modifier) {
                ResultScreen(recentMovieList, allMovieList)
            }
        }
        for(movie in recentMovieList) {
            val posterId = movie.posterResourceId
            homepageTestRule.onNodeWithTag(posterId.toString()).assertIsDisplayed()
            homepageTestRule.onNodeWithTag(movie.title).assertIsDisplayed()
        }
        for(movie in allMovieList) {
            val posterId = movie.posterResourceId
            homepageTestRule.onNodeWithTag(posterId.toString()).assertIsDisplayed()
            homepageTestRule.onNodeWithTag(movie.title).assertIsDisplayed()
        }
    }
}