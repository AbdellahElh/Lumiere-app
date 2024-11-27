package com.example.riseandroid.ui.screens.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.model.Program
import com.example.riseandroid.ui.screens.homepage.components.ListAllMovies
import com.example.riseandroid.ui.screens.homepage.components.MoviePosterItem
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
            val allMoviesNonRecent by homepageViewModel.allMovies.collectAsState()

            ResultScreen(
                navController = navController,
                recentMovieList = recentMovies,
                modifier = modifier,
                homepageViewModel = homepageViewModel,
                allMoviesNonRecent =allMoviesNonRecent
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
    recentMovieList: List<MoviePoster>,
    allMoviesNonRecent:List<MovieModel>,
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

            }
            Spacer(modifier = Modifier.height(33.dp))
            ToggleFilmOrEvent(isFilms) { isFilms = !isFilms }
            Spacer(modifier = Modifier.height(50.dp))
            if (isFilms){

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    items(recentMovieList) { poster ->
                        MoviePosterItem(
                            moviePoster = poster,
                            navController = navController,
                            modifier = Modifier
                                .width(150.dp)
                        )
                    }
                }

                TitleText(title = stringResource(R.string.alle_films_title), modifier = Modifier)

                MoviesFilters(homepageViewModel=homepageViewModel)

                ListAllMovies(
                    allMoviesNonRecent = allMoviesNonRecent,
                    navController = navController,
                    modifier = Modifier
                        .padding(posterImagePadding)
                        .height(400.dp)
                )
            }
            else{

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
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

                                        }
                                    )
                                }
                            }
                        }
                    }


                }


            }

        }
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
        color = Color.White,
        modifier = modifier.padding(16.dp)
    )
}
