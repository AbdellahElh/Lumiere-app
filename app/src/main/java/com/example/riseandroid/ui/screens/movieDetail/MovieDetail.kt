package com.example.riseandroid.ui.screens.movieDetail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.riseandroid.R
import com.example.riseandroid.model.Movie
import com.example.riseandroid.network.ResponseMovie
import com.example.riseandroid.ui.screens.account.AuthState
import com.example.riseandroid.ui.screens.account.AuthViewModel
import com.example.riseandroid.ui.screens.homepage.ErrorScreen
import com.example.riseandroid.ui.screens.homepage.LoadingScreen
import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModel
import com.example.riseandroid.util.isNetworkAvailable

@Composable
fun MovieDetailScreen(
    movieId: Int,
    navController: NavController,
    viewModel: MovieDetailViewModel = viewModel(
        factory = MovieDetailViewModel.provideFactory(movieId)
    ),
    watchlistViewModel: WatchlistViewModel,
    authViewModel: AuthViewModel,
) {
    val watchlistState by watchlistViewModel.watchlist.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    val isUserLoggedIn = authState is AuthState.Authenticated
    val context = LocalContext.current
    val isSyncing by watchlistViewModel.isSyncing.collectAsState()
    val isNetworkAvailable = remember { mutableStateOf(isNetworkAvailable(context)) }

    LaunchedEffect(movieId) {
        watchlistViewModel.syncWatchlist()
    }

    LaunchedEffect(watchlistViewModel) {
        watchlistViewModel.eventFlow.collect { event ->
            when (event) {
                is WatchlistViewModel.WatchlistEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val isInWatchlist = remember(watchlistState, movieId) {
        watchlistState.any { it.id == movieId }
    }

    when (val uiState = viewModel.movieDetailUiState) {
        is MovieDetailUiState.Loading -> LoadingScreen()
        is MovieDetailUiState.Error -> ErrorScreen()
        is MovieDetailUiState.Success -> {
            val movie = uiState.specificMovie

            MovieDetailContent(
                movie = movie,
                navController = navController,
                isInWatchlist = isInWatchlist,
                isUserLoggedIn = isUserLoggedIn,
                isSyncing = isSyncing,
                isNetworkAvailable = isNetworkAvailable.value,
                onWatchlistClick = {
                    Log.d("MovieDetailScreen", "Bookmark button clicked for movie ID: $movieId")
                    if (isUserLoggedIn) {
                        watchlistViewModel.toggleMovieInWatchlist(movie)
                        val message = if (isInWatchlist) {
                            "${movie.title} is verwijderd uit je watchlist"
                        } else {
                            "${movie.title} is toegevoegd aan je watchlist"
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Log in om films toe te voegen aan je watchlist",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailContent(
    movie: ResponseMovie,
    navController: NavController,
    isInWatchlist: Boolean,
    isUserLoggedIn: Boolean,
    isSyncing: Boolean,
    isNetworkAvailable: Boolean,
    onWatchlistClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(14.dp))
                MovieDetailHeader(
                    navController = navController,
                    isInWatchlist = isInWatchlist,
                    isUserLoggedIn = isUserLoggedIn,
                    onWatchlistClick = onWatchlistClick,
                    onBackClick = { navigateBack(navController) },
                    isSyncing = isSyncing,
                    isNetworkAvailable = isNetworkAvailable
                )
                Spacer(modifier = Modifier.height(32.dp))
                MoviePoster(movie)
                Spacer(modifier = Modifier.height(20.dp))
                MovieInfo(movie)
                Spacer(modifier = Modifier.height(10.dp))
                MovieDescription(movie, isExpanded) { isExpanded = !isExpanded }
                Spacer(modifier = Modifier.height(20.dp))

                if (movie.eventId != 0) {
                    Button(
                        onClick = {
                            navController.navigate("eventDetail/${movie.eventId}")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE5CB77),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 15.dp)
                    ) {
                        Text(
                            text = "Event Beschikbaar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                NextStepButton(
                    isUserLoggedIn = isUserLoggedIn,
                    onClick = { showBottomSheet = true }
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}


@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(movie.posterResourceId),
            contentDescription = "Poster of ${movie.title}",
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = movie.title,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
    }
}



@Composable
fun MovieDetailHeader(
    navController: NavController,
    isInWatchlist: Boolean,
    isUserLoggedIn: Boolean,
    isSyncing: Boolean,
    isNetworkAvailable: Boolean,
    onWatchlistClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.btn_back),
            contentDescription = "Back",
            modifier = Modifier
                .size(24.dp)
                .clickable { onBackClick() }
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Details Movie",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )
        Spacer(modifier = Modifier.weight(1f))

        if (isUserLoggedIn && isNetworkAvailable) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(enabled = !isSyncing) { onWatchlistClick() },
                contentAlignment = Alignment.Center
            ) {
                if (isSyncing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Image(
                        painter = painterResource(
                            id = if (isInWatchlist) R.drawable.btn_bookmark_filled else R.drawable.btn_bookmark_outline
                        ),
                        contentDescription = "Bookmark",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}



@Composable
fun MoviePoster(movie: ResponseMovie) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberImagePainter(movie.coverImageUrl),
            contentDescription = "Movie Poster",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(400.dp)
                .clip(RoundedCornerShape(20.dp))
        )
    }
}


@Composable
fun MovieInfo(movie: ResponseMovie) {
    Text(
        text = movie.title,
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White
    )

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Directeur: ${movie.director.orEmpty()}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = Color(0xFFBABFC9)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
    ) {
        Text(
            text = "${movie.genre.orEmpty()}",
            fontSize = 16.sp,
            color = Color(0xFFB2B5BB),
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF252932))
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "${movie.duration}",
            fontSize = 14.sp,
            color = Color(0xFFB2B5BB),
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF252932))
                .padding(8.dp)
        )
    }
}

@Composable
fun MovieDescription(movie: ResponseMovie, isExpanded: Boolean, onToggleExpand: () -> Unit) {
    Text(
        text = "Beschrijving",
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White,
        modifier = Modifier.padding(top = 14.dp)
    )

    val displayedDescription =
        if (isExpanded) movie.description.orEmpty() else movie.description.orEmpty().take(100)

    Text(
        text = if (isExpanded) displayedDescription else "$displayedDescription...",
        fontSize = 15.sp,
        fontWeight = FontWeight.Light,
        color = Color(0xFF696D74),
        modifier = Modifier.padding(top = 16.dp)
    )

    if (movie.description.orEmpty().length > 100) {
        Text(
            text = if (isExpanded) "Lees Minder" else "Lees Meer",
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
            color = Color(0xFFE5CB77),
            modifier = Modifier
                .clickable { onToggleExpand() }
        )
    }
}

@Composable
fun NextStepButton(isUserLoggedIn: Boolean, onClick: () -> Unit) {
    val context = LocalContext.current

    Button(
        onClick = {
            if (isUserLoggedIn) {
                onClick()
            } else {
                Toast.makeText(
                    context,
                    "U moet ingelogd zijn om door te gaan naar de volgende stap",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE5CB77),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(vertical = 15.dp)
    ) {
        Text(
            text = "Volgende Stap",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


fun navigateBack(navController: NavController) {
    navController.popBackStack()
}






