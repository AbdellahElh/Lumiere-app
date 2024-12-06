package com.example.riseandroid.ui.screens.homepage


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.riseandroid.R
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.ui.screens.homepage.components.CinemaDropdownMenu
import com.example.riseandroid.ui.screens.homepage.components.EventItem
import com.example.riseandroid.ui.screens.homepage.components.ListAllMovies
import com.example.riseandroid.ui.screens.homepage.components.MoviePosterItem
import com.example.riseandroid.ui.screens.homepage.components.MoviesFilters
import com.example.riseandroid.ui.screens.homepage.components.SlidingButton
import com.example.riseandroid.ui.screens.homepage.components.SlidingButtonForHomepage
import com.example.riseandroid.ui.screens.movieProgram.MovieProgram


@Composable
fun Homepage(
    goToMovieDetail: (id: String) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    selectedTab: Int = 0,
    homepageViewModel: HomepageViewModel = viewModel(factory = HomepageViewModel.Factory),
) {
    val homepageUiState = homepageViewModel.homepageUiState

    when (homepageUiState) {
        is HomepageUiState.Succes -> {
            val recentMovies by homepageViewModel.recentMovies.collectAsState()
            val allMoviesNonRecent by homepageViewModel.allMovies.collectAsState()

            ResultScreen(
                goToMovieDetail = goToMovieDetail,
                navController = navController,
                recentMovieList = recentMovies,
                modifier = modifier,
                homepageViewModel = homepageViewModel,
                allMoviesNonRecent = allMoviesNonRecent,
                selectedTab = selectedTab,
            )
        }
        is HomepageUiState.Loading -> LoadingScreen()
        else -> ErrorScreen()
    }
}



@Composable
fun ResultScreen(
    goToMovieDetail: (id: String) -> Unit,
    navController: NavHostController,
    recentMovieList: List<MoviePoster>,
    allMoviesNonRecent: List<MovieModel>,
    modifier: Modifier = Modifier,
    homepageViewModel: HomepageViewModel = viewModel(
        factory = HomepageViewModel.Factory
    ),
    selectedTab: Int,
) {
    val layoutDirection = LocalLayoutDirection.current
    val posterImagePadding = dimensionResource(R.dimen.image_padding)

    Surface(
        modifier = modifier
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
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
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
                0 -> { // Films Tab
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        TitleText("Binnenkort")
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

                        TitleText(title = stringResource(R.string.alle_films_title))

                        ListAllMovies(
                            allMoviesNonRecent = allMoviesNonRecent,
                            goToMovieDetail=goToMovieDetail,
                            modifier = Modifier
                                .padding(posterImagePadding)
                                .height(400.dp)
                        )
                    }
                }

                1 -> {
                    MovieProgram(
                        goToMovieDetail = goToMovieDetail,
                        modifier = modifier,
                    )
                }

                2 -> { // Events Tab
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        TitleText(title = "Events")

                        CinemaDropdownMenu(
                            options = homepageViewModel.options,
                            selectedOption = homepageViewModel.selectedCinema.collectAsState().value,
                            onOptionSelected = { newCinema ->
                                homepageViewModel.updateSelectedCinema(newCinema)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        homepageViewModel.filteredEvents.collectAsState().value.forEach { event ->
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


@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welkom bij Lumière",
                fontSize = 16.sp,
                color = Color(0xFFAFAFAF)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.lumiere_logo),
            contentDescription = "logo",
            modifier = Modifier.size(100.dp)
        )
    }
}


@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize() // Ensure the box takes up the full screen
    ) {
        Image(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center) // Center the image in the Box
                .testTag("LoadingImage"),
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.testTag("ErrorColumn"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}
@Composable
fun ToggleFilmOrEvent(isFilms: Boolean, onToggle: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        SlidingButton(
            isSelected = isFilms,
            onToggle = onToggle,
            leftText = "Films",
            rightText = "Programma"
        )
    }
}

@Composable
fun TitleText(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 28.sp,
        //fontFamily = Fontfamily.inter,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier.padding(16.dp)
    )
}