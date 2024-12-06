package com.example.riseandroid.ui.screens.homepage.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.R
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp


@Composable
fun ListAllMovies(
    allMoviesNonRecent: List<MovieModel>,
    goToMovieDetail: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (allMoviesNonRecent.isEmpty()) {
        Text(
            text = "Geen films beschikbaar",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            color = androidx.compose.ui.graphics.Color.White,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    } else {
        LazyRow(modifier = modifier.fillMaxWidth()) {
            items(allMoviesNonRecent) { movie ->
                MoviePoster(
                    movie = movie,
                    goToMovieDetail=goToMovieDetail,
                    modifier = modifier
                        .padding(dimensionResource(R.dimen.image_padding))
                )
            }
        }
    }
}
