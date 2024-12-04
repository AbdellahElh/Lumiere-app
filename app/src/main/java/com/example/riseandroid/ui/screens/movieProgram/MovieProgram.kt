import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.R
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.ui.screens.homepage.ErrorScreen
import com.example.riseandroid.ui.screens.homepage.HomepageUiState
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import com.example.riseandroid.ui.screens.homepage.LoadingScreen
import com.example.riseandroid.ui.screens.homepage.ResultScreen
import com.example.riseandroid.ui.screens.homepage.TitleText
import com.example.riseandroid.ui.screens.homepage.components.ListAllMovies
import com.example.riseandroid.ui.screens.homepage.components.MoviesFilters
import com.example.riseandroid.ui.screens.movieProgram.MovieProgramViewModel
import com.example.riseandroid.ui.screens.movieProgram.ProgramUiState
import kotlinx.coroutines.flow.StateFlow


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
            modifier = Modifier.padding(start = 2.dp).size(40.dp),
            tint = Color.White
            )
    }
}
