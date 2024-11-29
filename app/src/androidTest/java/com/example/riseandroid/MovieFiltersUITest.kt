// MovieFiltersUITest.kt
package com.example.riseandroid

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.material3.Surface
import com.example.riseandroid.ui.screens.homepage.components.MoviesFilters
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.Modifier
import com.example.riseandroid.fake.FakeMoviePosterRepo
import com.example.riseandroid.fake.FakeMovieRepo
import com.example.riseandroid.mockdata.MovieListMock
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class MovieFiltersUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeHomepageViewModel: HomepageViewModel
    private lateinit var fakeMovieRepo: FakeMovieRepo
    private lateinit var fakeMoviePosterRepo: FakeMoviePosterRepo

    @Before
    fun setup() {
        // Initialize MovieListMock to provide consistent mock data
        val movieListMock = MovieListMock()

        // Initialize FakeMovieRepo with mock MovieModel data
        fakeMovieRepo = FakeMovieRepo()

        // Initialize FakeMoviePosterRepo with mock MoviePoster data
        fakeMoviePosterRepo = FakeMoviePosterRepo()

        // Instantiate HomepageViewModel with the fake repositories
        fakeHomepageViewModel = HomepageViewModel(fakeMovieRepo, fakeMoviePosterRepo)
    }

    @Test
    fun filters_are_displayed_on_screen() = runTest {
        // Set the composable content with the fake ViewModel
        composeTestRule.setContent {
            Surface(modifier = Modifier) {
                MoviesFilters(homepageViewModel = fakeHomepageViewModel)
            }
        }

        // Assert that all filter components are displayed
        composeTestRule.onNodeWithContentDescription("Selecteer datum").assertIsDisplayed()
        composeTestRule.onNodeWithText("Selecteer Cinema").assertIsDisplayed()
        composeTestRule.onNodeWithText("Toepassen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("MovieTitleInput").assertIsDisplayed()
    }

    @Test
    fun testFiltersSelection() = runTest {
        // Set the composable content with the fake ViewModel
        composeTestRule.setContent {
            Surface(modifier = Modifier) {
                MoviesFilters(homepageViewModel = fakeHomepageViewModel)
            }
        }

        // Interact with the Cinema filter dropdown
        composeTestRule.onNodeWithText("Selecteer Cinema").assertIsDisplayed()
        composeTestRule.onNodeWithText("Selecteer Cinema").performClick()

        // Verify that all cinema options are present
        composeTestRule.onNodeWithText("Brugge").assertExists()
        composeTestRule.onNodeWithText("Antwerpen").assertExists()
        composeTestRule.onNodeWithText("Mechelen").assertExists()
        composeTestRule.onNodeWithText("Cinema Cartoons").assertExists()

        // Verify that "Antwerpen" is not initially selected
        composeTestRule.onNodeWithContentDescription("SelectedAntwerpen").assertDoesNotExist()

        // Select "Antwerpen" and verify selection
        composeTestRule.onNodeWithText("Antwerpen").performClick()
        composeTestRule.onNodeWithContentDescription("SelectedAntwerpen").assertExists()

        // Interact with the Movie Title input field
        composeTestRule.onNodeWithTag("MovieTitleInput").assertTextContains("Film titel...")
        composeTestRule.onNodeWithTag("MovieTitleInput").performClick()
        composeTestRule.onNodeWithTag("MovieTitleInput").performTextInput("Inception")
        composeTestRule.onNodeWithTag("MovieTitleInput").assertTextEquals("Inception")

        // Apply the filters and verify the button is clickable
        composeTestRule.onNodeWithText("Toepassen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Toepassen").performClick()

    }
}
