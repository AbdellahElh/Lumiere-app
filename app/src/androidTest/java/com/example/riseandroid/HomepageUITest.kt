package com.example.riseandroid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.riseandroid.mockdata.EventListMock
import com.example.riseandroid.mockdata.MovieListMock
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.ui.screens.homepage.Header
import com.example.riseandroid.ui.screens.homepage.TitleText
import com.example.riseandroid.ui.screens.homepage.components.EventItem
import com.example.riseandroid.ui.screens.homepage.components.ListAllMovies
import com.example.riseandroid.ui.screens.homepage.components.MoviePosterItem
import com.example.riseandroid.ui.screens.homepage.components.SlidingButtonForHomepage
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomepageUITest {
    @get:Rule
    val homepageTestRule = createComposeRule()
    lateinit var navController: TestNavHostController

    val recentMovieList = MovieListMock().LoadRecentMoviesMock()
    val allMoviesList = MovieListMock().LoadAllMoviesMock()
    val eventsList = EventListMock().LoadAllEventsMock()

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun titles_Placed_On_Homescreen() {
        homepageTestRule.setContent {
            Surface(modifier = Modifier) {
                FakeResultScreen(
                    navController = navController,
                    recentMovieList = recentMovieList,
                    allMoviesNonRecent = allMoviesList,
                    eventsList = eventsList,
                    selectedTab = 0
                )
            }
        }
        homepageTestRule.onNodeWithText("Films").assertIsDisplayed()
        homepageTestRule.onNodeWithText("Programma").assertIsDisplayed()
        homepageTestRule.onNodeWithText("Events").assertIsDisplayed()
    }

    @Test
    fun list_Of_Movies_Is_Shown() {
        homepageTestRule.setContent {
            Surface(modifier = Modifier) {
                FakeResultScreen(
                    navController = navController,
                    recentMovieList = recentMovieList,
                    allMoviesNonRecent = allMoviesList,
                    eventsList = eventsList,
                    selectedTab = 0
                )
            }
        }

        val mostNonRecentMovie = allMoviesList.first()
        homepageTestRule.onNodeWithContentDescription(mostNonRecentMovie.title).assertIsDisplayed()
    }

    @Test
    fun list_Of_Events_Is_Shown_On_Events_Tab() {
        homepageTestRule.setContent {
            Surface(modifier = Modifier) {
                FakeResultScreen(
                    navController = navController,
                    recentMovieList = recentMovieList,
                    allMoviesNonRecent = allMoviesList,
                    eventsList = eventsList,
                    selectedTab = 2
                )
            }
        }

        val firstEvent = eventsList.first()
        homepageTestRule.onNodeWithText(firstEvent.title).assertIsDisplayed()
    }
}


@Composable
fun FakeResultScreen(
    navController: NavHostController,
    recentMovieList: List<MoviePoster>,
    allMoviesNonRecent: List<MovieModel>,
    eventsList: List<EventModel>,
    selectedTab: Int
) {
    val layoutDirection = LocalLayoutDirection.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateEndPadding(layoutDirection),
            )
            .semantics { contentDescription = "Home Screen" },
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header()

            Spacer(modifier = Modifier.height(16.dp))

            SlidingButtonForHomepage(
                selectedIndex = selectedTab,
                onToggle = { newTab ->
                    navController.navigate("homepage?selectedTab=$newTab") {
                        popUpTo("homepage") { inclusive = true }
                    }
                },
                options = listOf("Films", "Programma", "Events")
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            items(recentMovieList) { poster ->
                                MoviePosterItem(
                                    moviePoster = poster,
                                    navController = navController,
                                    modifier = Modifier.width(150.dp)
                                )
                            }
                        }

                        TitleText(title = "Alle Films")

                        ListAllMovies(
                            allMoviesNonRecent = allMoviesNonRecent,
                            navController = navController,
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.image_padding))
                                .height(400.dp)
                        )
                    }
                }

                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        TitleText(title = "Programma")
                        // Mock of lege inhoud hier
                    }
                }

                2 -> { // Events Tab
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        TitleText(title = "Events")

                        eventsList.forEach { event ->
                            EventItem(
                                event = event,
                                navController = navController,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}



