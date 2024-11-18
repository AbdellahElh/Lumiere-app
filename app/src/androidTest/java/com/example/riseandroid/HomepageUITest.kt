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

    val recentMovieList = MovieListMock().LoadProgramsMock()
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
                    recentMovieList =recentMovieList,
                    programList = recentMovieList,
                    allMoviesNonRecent = allMoviesList,
                    modifier = Modifier,
                )
            }
        }
        homepageTestRule.onNodeWithText("Nieuwe films").assertIsDisplayed()
        homepageTestRule.onNodeWithText("Alle films").assertIsDisplayed()
    }

    @Test
    fun list_Of_Movies_Is_Shown(){
        homepageTestRule.setContent {
            Surface(modifier = Modifier) {
                ResultScreen(
                    navController = navController,
                    recentMovieList =recentMovieList,
                    programList = recentMovieList,
                    allMoviesNonRecent = allMoviesList,
                    modifier = Modifier,
                )
            }
        }
        val mostRecentMovie = recentMovieList.first()
            homepageTestRule.onNodeWithTag(mostRecentMovie.movie.posterResourceId.toString()).assertIsDisplayed()
            homepageTestRule.onNodeWithTag(mostRecentMovie.movie.title).assertIsDisplayed()

        val mostNonRecentMovie = allMoviesList.first()
            homepageTestRule.onNodeWithContentDescription(mostNonRecentMovie.name).assertIsDisplayed()


    }

//    @Test
//    fun loadingScreen_Is_Shown_When_Loading() {
//        val homepageViewModel = HomepageViewModel(
//            FakeNetworkMoviesRepository(countDownLatch),
//        )
//        homepageTestRule.setContent {
//            Surface(modifier = Modifier) {
//                Homepage(homepageViewModel = homepageViewModel)
//            }
//        }
//        homepageTestRule.onNodeWithTag("LoadingImage").assertIsDisplayed()
//    }

//    @Test
//    fun errorScreen_Show_When_An_Error_Occurs() {
//        homepageTestRule.setContent {
//            Surface(modifier = Modifier) {
//                Homepage()
//            }
//        }
//        homepageTestRule.onNodeWithTag("ErrorColumn").assertIsDisplayed()
//    }
}