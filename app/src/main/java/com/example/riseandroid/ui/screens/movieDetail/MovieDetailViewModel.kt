package com.example.riseandroid.ui.screens.movieDetail

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
import com.example.riseandroid.model.Program
import com.example.riseandroid.repository.IMovieRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface MovieDetailUiState {
    data class Success(val specificMovie: MovieModel, val programList: Flow<List<Program>>) : MovieDetailUiState
    object Error : MovieDetailUiState
    object Loading : MovieDetailUiState
}

class MovieDetailViewModel(
    private val movieId: Int,
    private val movieRepo: IMovieRepo, // MoviesRepository wordt niet meer gebruikt!!!
    private val programRepository: ProgramRepository
) : ViewModel() {

    var movieDetailUiState: MovieDetailUiState by mutableStateOf(MovieDetailUiState.Loading)
        private set

    private val _programList = MutableStateFlow<List<Program>>(emptyList())
    val programList: StateFlow<List<Program>> = _programList.asStateFlow()

    private val _allMovies = MutableStateFlow<List<MovieModel>>(emptyList()) // Gebruik MovieModel
    val allMovies: StateFlow<List<MovieModel>> = _allMovies.asStateFlow()

    init {
        getMovieDetails()
    }

    private fun getMovieDetails() {
        viewModelScope.launch {
            movieDetailUiState = MovieDetailUiState.Loading
            try {
                // Ophalen van de specifieke MovieModel
                val specificMovie = movieRepo.getSpecificMovie(movieId)
                // Ophalen van programma's voor de specifieke movie
                val programs = programRepository.getProgramsForMovie(movieId).firstOrNull() ?: emptyList()

                if (specificMovie != null) {
                    _programList.value = programs
                    movieDetailUiState = MovieDetailUiState.Success(specificMovie, programRepository.getProgramsForMovie(movieId))
                } else {
                    movieDetailUiState = MovieDetailUiState.Error
                }
            } catch (e: IOException) {
                movieDetailUiState = MovieDetailUiState.Error
            } catch (e: HttpException) {
                movieDetailUiState = MovieDetailUiState.Error
            }
        }
    }

    companion object {
        fun provideFactory(movieId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                val movieRepo = application.container.movieRepo
                val programRepository = application.container.programRepository

                MovieDetailViewModel(movieId, movieRepo, programRepository)
            }
        }
    }
}

class MoviesViewModel(private val movieRepo: IMovieRepo) : ViewModel() {

    private val _allMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val allMovies: StateFlow<List<MovieModel>> = _allMovies

    init {
        loadAllMovies()
    }

    private fun loadAllMovies() {
        viewModelScope.launch {
            movieRepo.getAllMoviesList("", emptyList()).collect { movies ->
                _allMovies.value = movies
            }
        }
    }
}
