package com.example.riseandroid.ui.screens.homepage.components

import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.riseandroid.data.RiseDatabase
import com.example.riseandroid.data.entitys.MovieDao
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.network.MoviesApi
import com.example.riseandroid.network.ResponseMoviePoster
import com.example.riseandroid.repository.MovieRepo
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MoviePosterItem(
    moviePoster: MoviePoster,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    homepageViewModel: HomepageViewModel = viewModel(factory = HomepageViewModel.Factory),
) {
    Column(
        modifier = modifier
            .clickable {
                homepageViewModel.viewModelScope.launch {
                    val movieId = homepageViewModel.getEveryMovie().first {
                        it.id == moviePoster.id
                    }.id
                    navController.navigate("movieDetail/${movieId}")
                }
            }
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(moviePoster.cover),
            contentDescription = "Poster of movie ID ${moviePoster.id}",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = formatReleaseDate(moviePoster.releaseDate),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

fun formatReleaseDate(releaseDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(releaseDate)
        date?.let { outputFormat.format(it) } ?: "Unknown Date"
    } catch (e: Exception) {
        "Invalid Date"
    }
}
