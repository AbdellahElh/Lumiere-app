package com.example.riseandroid.ui.screens.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.riseandroid.R
import com.example.riseandroid.model.Program
import com.example.riseandroid.ui.screens.homepage.components.SlidingButton
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun Homepage(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    homepageViewModel: HomepageViewModel = viewModel(factory = HomepageViewModel.Factory),
) {

    val homepageUiState = homepageViewModel.homepageUiState

    when (homepageUiState) {
        is HomepageUiState.Succes -> {
            val recentMovies by homepageViewModel.recentMovies.collectAsState()
            val nonRecentMovies by homepageViewModel.nonRecentMovies.collectAsState()
            val programList by homepageViewModel.programFilms.collectAsState()

            ResultScreen(
                navController = navController,
                recentMovieList = recentMovies,
                allMovieList = nonRecentMovies,
                programList = programList,
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
    allMovieList: List<Program>,
    programList: List<Program>,
    modifier: Modifier = Modifier,
    homepageViewModel: HomepageViewModel = viewModel(
        factory = HomepageViewModel.Factory
    ),

    ) {
    val layoutDirection = LocalLayoutDirection.current
    val posterImagePadding = dimensionResource(R.dimen.image_padding)
    val scrollState = rememberScrollState()
    var isFilms by remember { mutableStateOf(true) }
    var expandedLocaties by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Brugge") }
    val options = listOf("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")

    Surface(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
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
        Column() {
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
//                    painter = when (selectedOptionText) {
//                        "Brugge" -> painterResource(id = R.drawable.lumiere_brugge)
//                        "Antwerpen" -> painterResource(id = R.drawable.lumiere_antwerpen)
//                        "Mechelen" -> painterResource(id = R.drawable.lumiere_mechelen)
//                        "Cinema Cartoons" -> painterResource(id = R.drawable.lumiere_cinema_cartoons)
//                        else -> painterResource(id = R.drawable.lumiere_logo)
//                    },
                    painter = painterResource(id = R.drawable.lumiere_logo),
                    contentDescription = "logo",
//                    contentScale = ContentScale.FillBounds,
//                    modifier = Modifier
//                        .width(180.dp)
//                        .height(100.dp)
                )
            }
            Spacer(modifier = Modifier.height(33.dp))
            ToggleFilmOrEvent(isFilms) { isFilms = !isFilms }
            Spacer(modifier = Modifier.height(50.dp))
            if (isFilms){


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),

                            horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {

                        TitleText(
                            title = stringResource(R.string.nieuwe_films_title),
                            modifier = Modifier.offset(x = (-16).dp)
                        )                    }

                    Box(
                        modifier = Modifier
                            .border(
                                1.dp,
                                Color.White,
                                RoundedCornerShape(10.dp)
                            )


                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expandedLocaties,
                            onExpandedChange = { expandedLocaties = !expandedLocaties }
                        ) {
                            TextField(
                                value = selectedOptionText,
                                onValueChange = { newLocation ->
                                    selectedOptionText = newLocation
                                    homepageViewModel.updateMoviesLocation(newLocation)
                                },


                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocaties)
                                },
                                modifier = Modifier

                                    .menuAnchor()

                                    .clickable { expandedLocaties = !expandedLocaties },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),

                                textStyle =  TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                ),
                            )

                            ExposedDropdownMenu(
                                expanded = expandedLocaties,
                                onDismissRequest = { expandedLocaties = false }
                            ) {
                                options.forEach { film ->
                                    DropdownMenuItem(
                                        text = { Text(text = film) },
                                        onClick = {
                                            selectedOptionText = film
                                            expandedLocaties = false
                                            homepageViewModel.updateMoviesLocation(film)

                                        }
                                    )
                                }
                            }
                        }
                    }


                }
                MovieList(
                    movieList = recentMovieList,
                    navController = navController,
                    modifier = Modifier
                        .padding(posterImagePadding)
                        .height(400.dp)
                )

                TitleText(title = stringResource(R.string.alle_films_title), modifier = Modifier)
                MovieList(
                    movieList = allMovieList,
                    navController = navController,
                    modifier = Modifier
                        .padding(posterImagePadding)
                        .height(300.dp)
                )
            }
            else{

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding( 16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {

                        TitleText(title = "Programma",modifier = Modifier.offset(x = (-16).dp))
                    }
                    Spacer(modifier = Modifier.width(32.dp))
                    Box(
                        modifier = Modifier
                            .border(
                                1.dp,
                                Color.White,
                                RoundedCornerShape(10.dp)
                            )
                            .wrapContentSize()

                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expandedLocaties,
                            onExpandedChange = { expandedLocaties = !expandedLocaties }
                        ) {
                            TextField(
                                value = selectedOptionText,
                                onValueChange = { newLocation ->
                                    selectedOptionText = newLocation
                                    homepageViewModel.updateMoviesLocation(newLocation)
                                },


                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocaties)
                                },
                                modifier = Modifier
                                    .wrapContentSize()

                                    .menuAnchor()

                                    .clickable { expandedLocaties = !expandedLocaties },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),

                                textStyle =  TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                ),
                            )

                            ExposedDropdownMenu(
                                expanded = expandedLocaties,
                                onDismissRequest = { expandedLocaties = false }
                            ) {
                                options.forEach { film ->
                                    DropdownMenuItem(
                                        text = { Text(text = film) },
                                        onClick = {
                                            selectedOptionText = film
                                            expandedLocaties = false
                                            homepageViewModel.updateMoviesLocation(film)

                                        }
                                    )
                                }
                            }
                        }
                    }


                }
                MovieScheduleList(programList = programList, navController)

            }

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

