package com.example.riseandroid.fake

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.data.lumiere.ProgramRepository
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.repository.IMoviePosterRepo
import com.example.riseandroid.repository.IMovieRepo
import com.example.riseandroid.ui.screens.homepage.HomepageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FakeHomepageViewModel(
    private val movieRepo: IMovieRepo,
    private val moviePosterRepo: IMoviePosterRepo
) : ViewModel() {
    var homepageUiState: HomepageUiState by mutableStateOf(HomepageUiState.Loading)
        private set

    private val _recentMovies = MutableStateFlow<List<MoviePoster>>(emptyList())
    val recentMovies: StateFlow<List<MoviePoster>> = _recentMovies.asStateFlow()

    private val _allMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val allMovies: StateFlow<List<MovieModel>> = _allMovies.asStateFlow()

    private val _selectedDate = MutableStateFlow(getCurrentDate())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _searchTitle = MutableStateFlow<String?>(null)
    val searchTitle: StateFlow<String?> = _searchTitle.asStateFlow()

    private val _selectedCinemas = MutableStateFlow<List<String>>(emptyList())
    val selectedCinemas: StateFlow<List<String>> = _selectedCinemas.asStateFlow()

    val options = listOf("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")

    init {
        fetchAllMovies()
        fetchRecentMovies()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun fetchAllMovies() {
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading

            val cinemas = if (selectedCinemas.value.isEmpty()) {
                options
            } else {
                selectedCinemas.value
            }

            try {
                movieRepo.getAllMoviesList(selectedDate.value, cinemas, searchTitle.value)
                    .collect { movies ->
                        _allMovies.value = movies
                        homepageUiState = HomepageUiState.Success(
                            allMovies = allMovies,
                            recentMovies = recentMovies
                        )
                    }
            } catch (e: IOException) {
                homepageUiState = HomepageUiState.Error
            } catch (e: HttpException) {
                homepageUiState = HomepageUiState.Error
            }
        }
    }

    private fun fetchRecentMovies() {
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading
            try {
                moviePosterRepo.refreshMoviePosters()
                moviePosterRepo.getMoviePosters()
                    .collect { movies ->
                        _recentMovies.value = movies
                        homepageUiState = HomepageUiState.Success(
                            allMovies = allMovies,
                            recentMovies = recentMovies
                        )
                    }
            } catch (e: IOException) {
                homepageUiState = HomepageUiState.Error
            } catch (e: HttpException) {
                homepageUiState = HomepageUiState.Error
            }
        }
    }

    fun updateFilters(date: String, cinemas: List<String>, searchTitle: String) {
        _selectedDate.value = date
        _selectedCinemas.value = cinemas
        _searchTitle.value = searchTitle
        applyFilters()
    }

    private fun applyFilters() {
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading
            try {
                fetchAllMovies()
                homepageUiState = HomepageUiState.Success(
                    recentMovies = recentMovies,
                    allMovies = allMovies
                )
            } catch (e: IOException) {
                homepageUiState = HomepageUiState.Error
            } catch (e: HttpException) {
                homepageUiState = HomepageUiState.Error
            }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                val movieRepo = application.container.movieRepo
                val moviePosterRepo = application.container.moviePosterRepo
                FakeHomepageViewModel(movieRepo = movieRepo, moviePosterRepo = moviePosterRepo)
            }
        }
    }
}
