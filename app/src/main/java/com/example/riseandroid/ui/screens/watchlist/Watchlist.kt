package com.example.riseandroid.ui.screens.watchlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.riseandroid.model.Cinema
import com.example.riseandroid.model.MovieModel

@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel,
    allMovies: List<MovieModel>,
    onMovieClick: (Int) -> Unit,
    navController: NavController
) {
    val watchlistIds = viewModel.watchlist.collectAsState().value

    val watchlistMovies = allMovies
        .filter { it.id in watchlistIds }
        .map { movie ->
            MovieModel(
                id = movie.id,
                title = movie.title,
                coverImageUrl = movie.coverImageUrl,
                genre = movie.genre,
                duration = movie.duration,
                director = movie.director,
                description = movie.description,
                video = movie.video,
                videoPlaceholderUrl = movie.videoPlaceholderUrl,
                cast = movie.cast,
                cinemas = movie.cinemas.map { c ->
                    Cinema(
                        id = c.id,
                        name = c.name,
                        showtimes = c.showtimes
                    )
                }
            )
        }

    if (watchlistMovies.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Your watchlist is empty.",
                fontSize = 18.sp,
                color = androidx.compose.ui.graphics.Color.Gray
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(watchlistMovies) { movie ->
                WatchlistMovieItem(movie = movie, onClick = { onMovieClick(movie.id) })
            }
        }
    }
}


@Composable
fun WatchlistMovieItem(movie: MovieModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Film titel
        Text(
            text = movie.title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            color = androidx.compose.ui.graphics.Color.White
        )
    }
}

