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
import com.example.riseandroid.model.Program
import com.example.riseandroid.repository.ApiResource
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
                      val recentMovies: StateFlow<List<Program>>,
                      val programFilms: Flow<List<Program>>) : HomepageUiState
    object Error : HomepageUiState
    object Loading : HomepageUiState
}

class HomepageViewModel(
    private val programRepository: ProgramRepository,
    private val movieRepo: IMovieRepo,
) : ViewModel() {
    var homepageUiState: HomepageUiState by mutableStateOf(HomepageUiState.Loading)
        private set
    private val _recentMovies = MutableStateFlow<List<Program>>(emptyList())
    val recentMovies = _recentMovies.asStateFlow()

    private val _allMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val allMovies = _allMovies.asStateFlow()

    private val _programFilms = MutableStateFlow<List<Program>>(emptyList())
    val programFilms = _programFilms.asStateFlow()

    private val _selectedDate = MutableStateFlow(getCurrentDate())
    val selectedDate= _selectedDate.asStateFlow()

    private val _selectedCinemas = MutableStateFlow<List<String>>(emptyList())
    val options = listOf<String>("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")
    val selectedCinemas= _selectedCinemas.asStateFlow()

    fun updateFilters(date: String, cinemas: List<String>) {
        _selectedDate.value = date
        _selectedCinemas.value = cinemas
        Log.d("HomepageViewModel", "Filters updated: date=${_selectedDate.value}, cinemas=${_selectedCinemas.value}")
    }

    var moviesLocation: String by mutableStateOf("Brugge")
        private set

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    init {
        getMovies()
        getAllMoviesList()
    }

    private fun getAllMoviesList() {
        viewModelScope.launch {
            Log.d("HomepageViewModel", "getAllMovies: date=${_selectedDate.value}, cinemas=${_selectedCinemas.value}")
            homepageUiState = HomepageUiState.Loading

            val cinemas = if (selectedCinemas.value.isEmpty()) {
                listOf("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")
            } else {
                selectedCinemas.value
            }

            movieRepo.getAllMoviesList(selectedDate.value, cinemas)
                .collect { movies ->
                        _allMovies.value = movies
                        homepageUiState = HomepageUiState.Succes(
                            allMovies = _allMovies,
                            recentMovies = recentMovies,
                            programFilms = programFilms,
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
                    programFilms = programFilms,
                    allMovies = allMovies
                )
            } catch (e: IOException) {
                homepageUiState = HomepageUiState.Error
            } catch (e: HttpException) {
                homepageUiState = HomepageUiState.Error
            }
        }
    }



    fun getMovies() {
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading
            homepageUiState = try {
                val programFilms = programRepository.getProgramsLocation(moviesLocation)
                val movieList = programRepository.getMoviesLocation(moviesLocation)

                programFilms.collectLatest { _programFilms.value = it }
                movieList.collectLatest {
                    _recentMovies.value = it
                }
                HomepageUiState.Succes(
                    recentMovies = recentMovies,
                    programFilms = programFilms,
                    allMovies = allMovies
                )
            } catch (e: IOException) {
                HomepageUiState.Error
            } catch (e: HttpException) {
                HomepageUiState.Error
            }
        }
    }

    fun updateMoviesLocation(newLocation: String) {
        if (moviesLocation != newLocation) {
            moviesLocation = newLocation
            getMoviesByLocation()
        }
    }

    private fun getMoviesByLocation() {
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading
            homepageUiState = try {
                val programFilms = programRepository.getProgramsLocation(moviesLocation)
                val movieList = programRepository.getMoviesLocation(moviesLocation)

                programFilms.collectLatest { _programFilms.value = it }
                movieList.collectLatest {
                    _recentMovies.value = it
                }
                HomepageUiState.Succes(
                    recentMovies = recentMovies,
                    programFilms = programFilms,
                    allMovies = allMovies
                )
            } catch (e: IOException) {
                HomepageUiState.Error
            } catch (e: HttpException) {
                HomepageUiState.Error
            }
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                val programRepository = application.container.programRepository
                val movieRepo=application.container.movieRepo
                HomepageViewModel(programRepository = programRepository,movieRepo=movieRepo)
            }
        }
    }
}