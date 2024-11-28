package com.example.riseandroid

import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.riseandroid.fake.FakeHomepageViewModel
import com.example.riseandroid.mockdata.MovieListMock
import com.example.riseandroid.ui.screens.homepage.ResultScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch

class HomepageUITest {
    @get:Rule
    val homepageTestRule = createComposeRule()
    lateinit var navController: TestNavHostController

    val recentMovieList = MovieListMock().LoadRecentMoviesMock()
    val allMoviesList = MovieListMock().LoadAllMoviesMock()

    private lateinit var fakeHomepageViewModel: FakeHomepageViewModel
    private final val countDownLatch: CountDownLatch = CountDownLatch(1)


    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        //fakeHomepageViewModel = FakeHomepageViewModel(programRepository)
    }
    @Test
    fun titles_Placed_On_Homescreen() {
        homepageTestRule.setContent {
            Surface(modifier = Modifier) {
                ResultScreen(
                    navController = navController,
                    recentMovieList = recentMovieList,
                    allMoviesNonRecent = allMoviesList,
                    modifier = Modifier,
                )
            }
        }
        homepageTestRule.onNodeWithText("Alle films").assertIsDisplayed()
    }

    @Test
    fun list_Of_Movies_Is_Shown(){
        homepageTestRule.setContent {
            Surface(modifier = Modifier) {
                ResultScreen(
                    navController = navController,
                    recentMovieList =recentMovieList,
                    allMoviesNonRecent = allMoviesList,
                    modifier = Modifier,
                )
            }
        }

        val mostNonRecentMovie = allMoviesList.first()
            homepageTestRule.onNodeWithContentDescription(mostNonRecentMovie.title).assertIsDisplayed()


    }

}