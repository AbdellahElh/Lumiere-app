package com.example.riseandroid.ui.screens.movieProgram;

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.ui.screens.homepage.ErrorScreen
import com.example.riseandroid.ui.screens.homepage.LoadingScreen
import com.example.riseandroid.ui.screens.homepage.TitleText
import com.example.riseandroid.ui.screens.homepage.components.ListAllMovies
import com.example.riseandroid.ui.screens.homepage.components.MoviesFilters

@Composable
fun MovieProgram(
    goToMovieDetail: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    programViewModel: MovieProgramViewModel = viewModel(factory = MovieProgramViewModel.Factory),
) {

    val programUiState = programViewModel.programUiState

    when (programUiState) {
        is ProgramUiState.Succes -> {
            val allMoviesList by programUiState.movieList.collectAsState()

          ProgramResult(
              programViewModel=programViewModel,
              allMoviesList=allMoviesList,
              goToMovieDetail=goToMovieDetail
          )

        }
        is ProgramUiState.Loading -> LoadingScreen()
        else -> ErrorScreen()
    }
}


@Composable
fun ProgramResult(
    programViewModel: MovieProgramViewModel,
    allMoviesList: List<MovieModel>,
    goToMovieDetail: (id: String) -> Unit,
){
    var areFiltersVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        FiltersButtonToggle(areFiltersVisible=areFiltersVisible,
            onToggleFilters = { areFiltersVisible = it })

        if (areFiltersVisible) {
            MoviesFilters(viewModel = programViewModel)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(5.dp))

        ListAllMovies(
            allMoviesNonRecent = allMoviesList,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            goToMovieDetail = goToMovieDetail
        )
    }

}

@Composable
fun FiltersButtonToggle(
    areFiltersVisible: Boolean,
    onToggleFilters: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onToggleFilters(!areFiltersVisible) }
            .padding(bottom = 8.dp)
    ) {
        TitleText(title = "Filters", modifier = Modifier)

        Icon(
            imageVector = if (!areFiltersVisible) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
            contentDescription = "Show filters",
            modifier = Modifier.padding(start = 2.dp).size(40.dp)

            )
    }
}
