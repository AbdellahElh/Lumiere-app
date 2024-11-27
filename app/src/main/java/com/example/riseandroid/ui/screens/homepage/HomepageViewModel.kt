package com.example.riseandroid.ui.screens.homepage

import android.util.Log
import retrofit2.HttpException
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
import com.example.riseandroid.data.entitys.MovieDao
import com.example.riseandroid.data.lumiere.ProgramRepository
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.model.Program
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.IMoviePosterRepo
import com.example.riseandroid.repository.IMovieRepo
import com.example.riseandroid.repository.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed interface HomepageUiState {
    data class Succes(val allMovies: StateFlow<List<MovieModel>>,
                      val recentMovies: StateFlow<List<MoviePoster>>) : HomepageUiState
    object Error : HomepageUiState
    object Loading : HomepageUiState
}

class HomepageViewModel(
    private val movieRepo: IMovieRepo,
    private val moviePosterRepo: IMoviePosterRepo
) : ViewModel() {
    var homepageUiState: HomepageUiState by mutableStateOf(HomepageUiState.Loading)
        private set
    private val _recentMovies = MutableStateFlow<List<MoviePoster>>(emptyList())
    val recentMovies = _recentMovies.asStateFlow()

    private val _allMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val allMovies = _allMovies.asStateFlow()
//
//    private val _programFilms = MutableStateFlow<List<Program>>(emptyList())
//    val programFilms = _programFilms.asStateFlow()

    private val _selectedDate = MutableStateFlow(getCurrentDate())
    val selectedDate= _selectedDate.asStateFlow()

    private val _searchTitle = MutableStateFlow<String?>(null)
    val searchTitle: StateFlow<String?> = _searchTitle.asStateFlow()

    private val _selectedCinemas = MutableStateFlow<List<String>>(emptyList())
    val options = listOf<String>("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")
    val selectedCinemas= _selectedCinemas.asStateFlow()

    fun updateFilters(date: String, cinemas: List<String>,searchTitle:String) {
        _selectedDate.value = date
        _selectedCinemas.value = cinemas
        _searchTitle.value=searchTitle
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    init {

        getAllMoviesList()
        getNonRecentMovieList()
    }

    private fun getAllMoviesList() {
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading

            val cinemas = if (selectedCinemas.value.isEmpty()) {
                listOf("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")
            } else {
                selectedCinemas.value
            }

            movieRepo.getAllMoviesList(selectedDate.value, cinemas,searchTitle.value)
                .collect { movies ->
                    _allMovies.value = movies
                    homepageUiState = HomepageUiState.Succes(
                        allMovies = _allMovies,
                        recentMovies = recentMovies,
                    )

                }
        }
    }

    private fun getNonRecentMovieList(){
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading
            moviePosterRepo.refreshMoviePosters()
            moviePosterRepo.getMoviePosters()
                .collect { movies ->
                    _recentMovies.value = movies
                    homepageUiState = HomepageUiState.Succes(
                        allMovies = _allMovies,
                        recentMovies = recentMovies,
                    )

                }
        }
    }


    fun applyFilters() {
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading
            try {
                getAllMoviesList()
                homepageUiState = HomepageUiState.Succes(
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
                val movieRepo=application.container.movieRepo
                val moviePosterRepo = application.container.moviePosterRepo
                HomepageViewModel(movieRepo=movieRepo,
                    moviePosterRepo = moviePosterRepo)
            }
        }
    }
}