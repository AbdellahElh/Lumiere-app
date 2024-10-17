package com.example.riseandroid.screens.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.riseandroid.model.Movie
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.riseandroid.R
import com.example.riseandroid.repository.Datasource


@Composable
fun Homepage(
    navController: NavHostController,
    recentMovieList: List<Movie> = Datasource().LoadRecentMovies(),
    allMovieList: List<Movie> = Datasource().LoadAllMovies()
) {
    val layoutDirection = LocalLayoutDirection.current
    val posterImagePadding = dimensionResource(R.dimen.image_padding)
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
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
            )  .semantics{ contentDescription ="Home Screen" },
        color = MaterialTheme.colorScheme.background
    ) {
        Column() {
            TitleText(title = "Nieuwe films", modifier = Modifier)
            MovieList(
                movieList = recentMovieList,
                navController = navController,
                modifier = Modifier
                    .padding(posterImagePadding)
                    .height(350.dp)
            )
            TitleText(title = "Alle films", modifier = Modifier)
            MovieList(
                movieList = allMovieList,
                navController = navController,
                modifier = Modifier
                    .padding(posterImagePadding)
                    .height(250.dp)
            )
        }
    }
}

@Composable
fun MoviePosterCard(
    movie: Movie,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable {
            navController.navigate("movieDetail/${movie.movieId}")
        }
    ) {
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
fun MovieList(
    movieList: List<Movie>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier) {
        items(movieList) { movie ->
            MoviePosterCard(
                movie = movie,
                navController = navController,
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