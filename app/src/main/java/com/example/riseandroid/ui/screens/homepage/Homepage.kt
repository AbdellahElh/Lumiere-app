package com.example.riseandroid.ui.screens.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.riseandroid.model.Movie
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.riseandroid.R
import com.example.riseandroid.data.Datasource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch


@Composable
fun Homepage(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    homepageViewModel : HomepageViewModel = viewModel(
        factory = HomepageViewModel.Factory
    )
    ) {

    val homepageUiState =  homepageViewModel.homepageUiState

    when (homepageUiState) {
        is HomepageUiState.Succes -> {
            val recentMovies =
                homepageUiState.recentMovies.collectAsState(initial = emptyList())

            val nonRecentMovies =
                homepageUiState.nonRecentMovies.collectAsState(initial = emptyList())

            ResultScreen(recentMovies.value, nonRecentMovies.value)
        }
        is HomepageUiState.Loading -> LoadingScreen()
        else -> {ErrorScreen()}
    }

}


@Composable
fun ResultScreen(
    recentMovieList: List<Movie> = Datasource().LoadRecentMovies(),
    allMovieList: List<Movie> = Datasource().LoadNonRecentMovies(),
    modifier: Modifier = Modifier
) {
    val layoutDirection = LocalLayoutDirection.current
    val posterImagePadding = dimensionResource(R.dimen.image_padding)
    val scrollState = rememberScrollState()
    Surface(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .statusBarsPadding()
            .padding(
                start = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateEndPadding(layoutDirection),
            ),
        color = MaterialTheme.colorScheme.background
    ) {
        Column() {
            TitleText(title = stringResource(R.string.nieuwe_films_title), modifier = Modifier)
            MovieList(
                movieList = recentMovieList,
                modifier = Modifier
                    .padding(posterImagePadding)
                    .height(350.dp)
            )
            TitleText(title = stringResource(R.string.alle_films_title), modifier = Modifier)
            MovieList(
                movieList = allMovieList,
                modifier = Modifier
                    .padding(posterImagePadding)
                    .height(250.dp)
            )
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .size(200.dp)
            .testTag("LoadingImage"),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
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
fun MoviePosterCard(movie: Movie, modifier: Modifier = Modifier) {

    Column(modifier = modifier) {
        val posterId = movie.posterResourceId
        Image(
            painter = painterResource(posterId),
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .weight(0.75f)
                .clip(MaterialTheme.shapes.medium)
                .testTag(posterId.toString()),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = movie.title,
            modifier
                .weight(0.25f)
                .testTag(movie.title),
            color = Color.White,
        )
    }

}

@Composable
fun MovieList(movieList: List<Movie>, modifier: Modifier = Modifier) {
    LazyRow(modifier = modifier) {
        items(movieList) { m ->
            MoviePosterCard(
                movie = m,
                modifier = modifier
            )
        }
    }
}

@Composable
fun TitleText(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 35.sp,
        //fontFamily = Fontfamily.inter,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        modifier = modifier.padding(8.dp)
    )
}