package com.example.riseandroid.ui.screens.homepage

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.riseandroid.R
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.Program
import com.example.riseandroid.ui.screens.homepage.components.ListAllMovies
import com.example.riseandroid.ui.screens.homepage.components.MoviesFilters
import com.example.riseandroid.ui.screens.homepage.components.SlidingButton
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun Homepage(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    homepageViewModel: HomepageViewModel = viewModel(factory = HomepageViewModel.Factory),
) {

    val homepageUiState = homepageViewModel.homepageUiState

    when (homepageUiState) {
        is HomepageUiState.Succes -> {
            val recentMovies by homepageViewModel.recentMovies.collectAsState()
            val programList by homepageViewModel.programFilms.collectAsState()
            val allMoviesNonRecent by homepageViewModel.allMovies.collectAsState()
            val eventsList by homepageViewModel.events.collectAsState()

            Log.d("Homepage", "Events list size: ${eventsList.size}")

            ResultScreen(
                navController = navController,
                recentMovieList = recentMovies,
                programList = programList,
                allMoviesNonRecent = allMoviesNonRecent,
                eventsList = eventsList,
                modifier = modifier,
                homepageViewModel = homepageViewModel
            )
        }
        is HomepageUiState.Loading -> LoadingScreen()
        else -> ErrorScreen()
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    navController: NavHostController,
    recentMovieList: List<Program>,
    programList: List<Program>,
    allMoviesNonRecent: List<MovieModel>,
    eventsList: List<EventModel>,
    modifier: Modifier = Modifier,
    homepageViewModel: HomepageViewModel = viewModel(
        factory = HomepageViewModel.Factory
    ),
) {
    val layoutDirection = LocalLayoutDirection.current
    val posterImagePadding = dimensionResource(R.dimen.image_padding)
    var isFilms by remember { mutableStateOf(true) }
    var expandedLocaties by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Brugge") }

    val options = listOf("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")

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
        if (isFilms) {
            // Movies Tab Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header and Greeting
                HeaderSection(
                    selectedOptionText = selectedOptionText,
                    expandedLocaties = expandedLocaties,
                    onExpandedChange = { expandedLocaties = it },
                    onLocationSelected = { newLocation ->
                        selectedOptionText = newLocation
                        homepageViewModel.updateMoviesLocation(newLocation)
                    }
                )

                Spacer(modifier = Modifier.height(33.dp))

                ToggleFilmOrEvent(isFilms) { isFilms = !isFilms }

                Spacer(modifier = Modifier.height(50.dp))

                // Recent Movies Section
                MoviesSection(
                    recentMovieList = recentMovieList,
                    navController = navController,
                    homepageViewModel = homepageViewModel,
                    selectedOptionText = selectedOptionText,
                    options = options,
                    posterImagePadding = posterImagePadding
                )

                // All Movies Section
                TitleText(
                    title = stringResource(R.string.alle_films_title),
                    modifier = Modifier.padding(16.dp)
                )

                MoviesFilters(homepageViewModel = homepageViewModel)

                ListAllMovies(
                    allMoviesNonRecent = allMoviesNonRecent,
                    navController = navController,
                    modifier = Modifier
                        .padding(posterImagePadding)
                        .height(400.dp)
                )
            }
        } else {
            // Events Tab Content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Header and Greeting
                    HeaderSection(
                        selectedOptionText = selectedOptionText,
                        expandedLocaties = expandedLocaties,
                        onExpandedChange = { expandedLocaties = it },
                        onLocationSelected = { newLocation ->
                            selectedOptionText = newLocation
                            homepageViewModel.updateMoviesLocation(newLocation)
                        }
                    )

                    Spacer(modifier = Modifier.height(33.dp))

                    ToggleFilmOrEvent(isFilms) { isFilms = !isFilms }

                    Spacer(modifier = Modifier.height(50.dp))

                    TitleText(title = "Events", modifier = Modifier.padding(16.dp))
                }

                items(eventsList) { event ->
                    EventItem(event = event, navController = navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSection(
    selectedOptionText: String,
    expandedLocaties: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onLocationSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welkom terug,",
                fontSize = 16.sp,
                color = Color(0xFFAFAFAF)
            )
            Text(
                text = "Dion",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.lumiere_logo),
            contentDescription = "logo",
        )
    }

    // Location Dropdown
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        horizontalArrangement = Arrangement.Start,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Box(
//            modifier = Modifier
//                .border(
//                    1.dp,
//                    Color.White,
//                    RoundedCornerShape(10.dp)
//                )
//        ) {
//            ExposedDropdownMenuBox(
//                expanded = expandedLocaties,
//                onExpandedChange = onExpandedChange
//            ) {
//                TextField(
//                    value = selectedOptionText,
//                    onValueChange = {},
//                    readOnly = true,
//                    trailingIcon = {
//                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocaties)
//                    },
//                    modifier = Modifier
//                        .menuAnchor()
//                        .clickable { onExpandedChange(!expandedLocaties) },
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent
//                    ),
//                    textStyle = TextStyle(
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Medium,
//                        color = Color.White
//                    ),
//                )
//
//                ExposedDropdownMenu(
//                    expanded = expandedLocaties,
//                    onDismissRequest = { onExpandedChange(false) }
//                ) {
//                    val options = listOf("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")
//                    options.forEach { location ->
//                        DropdownMenuItem(
//                            text = { Text(text = location) },
//                            onClick = {
//                                onLocationSelected(location)
//                                onExpandedChange(false)
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
}

@Composable
fun MoviesSection(
    recentMovieList: List<Program>,
    navController: NavHostController,
    homepageViewModel: HomepageViewModel,
    selectedOptionText: String,
    options: List<String>,
    posterImagePadding: Dp
) {
    TitleText(
        title = stringResource(R.string.nieuwe_films_title),
        modifier = Modifier.padding(16.dp)
    )

    MovieList(
        movieList = recentMovieList,
        navController = navController,
        modifier = Modifier
            .padding(posterImagePadding)
            .height(400.dp)
    )
}

@Composable
fun EventList(
    eventsList: List<EventModel>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(eventsList) { event ->
            EventItem(event = event, navController = navController)
        }
    }
}

@Composable
fun EventItem(
    event: EventModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Log.d("EventItem", "Event: ${event.title}, coverImageUrl: ${event.cover}")
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navController.navigate("eventDetail/${event.id}") }
            .padding(16.dp)
    ) {
        if (!event.cover.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(event.cover),
                contentDescription = event.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = event.title ?: "Geen titel",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = event.description ?: "Geen beschrijving",
            fontSize = 16.sp,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = event.date ?: "Onbekend",
            fontSize = 16.sp,
            color = Color.White
        )
        Text(
            text = event.location ?: "Onbekend",
            fontSize = 16.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        val formattedPrice = event.price?.let {
            try {
                val priceValue = it.toDouble()
                String.format("€%.2f", priceValue)
            } catch (e: NumberFormatException) {
                it
            }
        } ?: "Onbekend"

        Text(
            text = "Prijs: $formattedPrice",
            fontSize = 16.sp,
            color = Color.White
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
            rightText = "Events"
        )
    }
}
@Composable
fun MoviePosterCard(movie: Program, navController: NavHostController, modifier: Modifier = Modifier) {
    println(movie)
    Column(modifier = modifier.clickable {
        navController.navigate("movieDetail/${movie.movie.movieId}")
    }
    )  {
        val posterId = movie.movie.posterResourceId
        val movieTitle = movie.movie.title
        Image(
            painter = painterResource(posterId),
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .weight(0.75f)
                .clip(MaterialTheme.shapes.medium)
                .testTag(posterId.toString())
                .testTag(movie.movie.posterResourceId.toString()) ,

            contentScale = ContentScale.Crop,
        )
        Text(
            text = movieTitle,
            modifier
                .weight(0.25f)
                .testTag(movieTitle),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }

}

@Composable
fun MovieList(movieList: List<Program>, navController: NavHostController, modifier: Modifier = Modifier) {
    LazyRow(modifier = modifier) {
        items(movieList) { m ->
            MoviePosterCard(
                movie = m,
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
        fontSize = 28.sp,
        //fontFamily = Fontfamily.inter,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        modifier = modifier.padding(16.dp)
    )
}
@Composable
fun MovieScheduleList(
    programList: List<Program>,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val groupedByDate = programList.groupBy { it.date }.toSortedMap()
    val dateFormatterInput = SimpleDateFormat("yyyy-MM-dd", Locale("nl"))
    val dateFormatterOutput = SimpleDateFormat("d MMMM yyyy", Locale("nl"))
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        groupedByDate.forEach { (date, programsByDate) ->
            val parsedDate = dateFormatterInput.parse(date)
            Text(
                text = dateFormatterOutput.format(parsedDate),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            val groupedByMovie = programsByDate.groupBy { it.movie.title }
            println(groupedByMovie)
            groupedByMovie.forEach { (movieTitle, moviePrograms) ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = movieTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 14.dp)
                    )

                    Image(
                        painter = painterResource(id = moviePrograms.first().movie.posterResourceId),
                        contentDescription = movieTitle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {navController.navigate("movieDetail/${moviePrograms.first().movie.movieId}")},
                        contentScale = ContentScale.Crop

                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    moviePrograms.forEach { program ->
                        Text(
                            text = "- ${program.hours} @ ${program.location}",
                            fontSize = 16.sp,
                            color = Color(0xFFAFAFAF),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

