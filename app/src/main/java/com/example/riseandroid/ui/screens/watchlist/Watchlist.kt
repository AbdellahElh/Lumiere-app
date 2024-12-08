package com.example.riseandroid.ui.screens.watchlist

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.model.MovieModel

@Composable
fun WatchlistScreen(
    onMovieClick: (Int) -> Unit,
    navController: NavController,
    watchlistViewModel: WatchlistViewModel = viewModel(
        factory = WatchlistViewModelFactory(
            watchlistRepo = (LocalContext.current.applicationContext as LumiereApplication).container.watchlistRepo,
            userManager = (LocalContext.current.applicationContext as LumiereApplication).container.userManager,
            application = LocalContext.current.applicationContext as Application,
        )
    )
) {
    val watchlistMovies by watchlistViewModel.watchlist.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        watchlistViewModel.refreshWatchlist()
        watchlistViewModel.eventFlow.collect { event ->
            when (event) {
                is WatchlistViewModel.WatchlistEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(navController)

        if (watchlistMovies.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Uw watchlist is leeg.",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(watchlistMovies) { movie ->
                    Column {
                        WatchlistMovieItem(
                            movie = movie,
                            onClick = { onMovieClick(movie.id) }
                        )
                        Divider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun TopBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE5CB77))
            .padding(vertical = 25.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Watchlist",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun WatchlistMovieItem(movie: MovieModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
            .testTag("MovieItem_${movie.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(movie.coverImageUrl),
            contentDescription = "${movie.title} Cover",
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = movie.title,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )
    }
}



