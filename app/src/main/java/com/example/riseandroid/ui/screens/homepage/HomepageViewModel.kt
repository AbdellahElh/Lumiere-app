package com.example.riseandroid.ui.screens.homepage

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.data.lumiere.ProgramRepository
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.model.Program
import com.example.riseandroid.repository.IMoviePosterRepo
import com.example.riseandroid.repository.IMovieRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed interface HomepageUiState {
    data class Success(
        val allMovies: List<MovieModel>,
        val moviePosters: List<MoviePoster>,
        val programFilms: List<Program>
    ) : HomepageUiState
    object Error : HomepageUiState
    object Loading : HomepageUiState
}

class HomepageViewModel(
    private val programRepository: ProgramRepository,
    private val movieRepo: IMovieRepo,
    private val moviePosterRepo: IMoviePosterRepo
) : ViewModel() {
    private val _homepageUiState = MutableStateFlow<HomepageUiState>(HomepageUiState.Loading)
    val homepageUiState: StateFlow<HomepageUiState> = _homepageUiState.asStateFlow()

    private val _selectedDate = MutableStateFlow(getCurrentDate())
    val selectedDate = _selectedDate.asStateFlow()

    private val _selectedCinemas = MutableStateFlow<List<String>>(emptyList())
    val options = listOf("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")
    val selectedCinemas = _selectedCinemas.asStateFlow()

    private val _selectedMovie = MutableStateFlow<MovieModel?>(null)
    val selectedMovie: StateFlow<MovieModel?> = _selectedMovie.asStateFlow()

    var moviesLocation: String by mutableStateOf("Brugge")
        private set

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                // Explicitly refresh movie posters
                moviePosterRepo.refreshMoviePosters()

                val cinemas = if (_selectedCinemas.value.isEmpty()) {
                    listOf("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")
                } else {
                    _selectedCinemas.value
                }

                val allMoviesFlow = movieRepo.getAllMoviesList(_selectedDate.value, cinemas)
                val moviePostersFlow = moviePosterRepo.getMoviePosters()
                val programFilmsFlow = programRepository.getProgramsLocation(moviesLocation)

                combine(
                    allMoviesFlow,
                    moviePostersFlow,
                    programFilmsFlow
                ) { allMovies, moviePosters, programFilms ->
                    HomepageUiState.Success(
                        allMovies = allMovies,
                        moviePosters = moviePosters,
                        programFilms = programFilms
                    )
                }.collect { uiState ->
                    _homepageUiState.value = uiState
                }

            } catch (e: Exception) {
                _homepageUiState.value = HomepageUiState.Error
                Log.e("HomepageViewModel", "Error in fetchData", e)
            }
        }
    }



    fun updateFilters(date: String, cinemas: List<String>) {
        viewModelScope.launch {
            _selectedDate.value = date
            _selectedCinemas.value = cinemas
            Log.d(
                "HomepageViewModel",
                "Filters updated: date=${_selectedDate.value}, cinemas=${_selectedCinemas.value}"
            )
            fetchData()
        }
    }

    fun updateMoviesLocation(newLocation: String) {
        viewModelScope.launch {
            if (moviesLocation != newLocation) {
                moviesLocation = newLocation
                Log.d("HomepageViewModel", "Movies location updated to $newLocation")
                fetchData()
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LumiereApplication)
                val programRepository = application.container.programRepository
                val movieRepo = application.container.movieRepo
                val moviePosterRepo = application.container.moviePosterRepo
                HomepageViewModel(
                    programRepository = programRepository,
                    movieRepo = movieRepo,
                    moviePosterRepo = moviePosterRepo
                )
            }
        }
    }
}
