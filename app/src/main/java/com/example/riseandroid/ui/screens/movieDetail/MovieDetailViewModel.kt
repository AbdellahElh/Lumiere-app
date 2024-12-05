package com.example.riseandroid.ui.screens.movieDetail

import android.app.Application
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
import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.data.lumiere.ProgramRepository
import com.example.riseandroid.model.Movie
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
    private val movieRepo: IMovieRepo,
    private val programRepository: ProgramRepository
) : ViewModel() {

    var movieDetailUiState: MovieDetailUiState by mutableStateOf(MovieDetailUiState.Loading)
        private set

    private val _programList = MutableStateFlow<List<Program>>(emptyList())
    val programList: StateFlow<List<Program>> = _programList.asStateFlow()

    private val _selectedMovie = MutableStateFlow<MovieModel>(MovieModel(
        id = 0,
        eventId = 0,
        title = "",
        coverImageUrl = "",
        genre = "",
        duration = 0,
        director = "",
        description = "",
        videoPlaceholderUrl = "",
        cast = emptyList(),
        cinemas = emptyList(),
        releaseDate = "",
        bannerImageUrl = "",
        posterImageUrl = "",
        movieLink = ""
    ))
    val selectedMovie: StateFlow<MovieModel> = _selectedMovie.asStateFlow()

    init {
        getMovieDetails()
    }

    private fun getMovieDetails() {
        viewModelScope.launch {
            movieDetailUiState = MovieDetailUiState.Loading
            try {
                val specificMovie = movieRepo.getMovieById(movieId)
                val programs = programRepository.getProgramsForMovie(movieId.toLong()).firstOrNull() ?: emptyList()

                if (specificMovie != null) {
                    _programList.value = programs
                    _selectedMovie.value = specificMovie
                    movieDetailUiState = MovieDetailUiState.Success(
                        specificMovie,
                        programRepository.getProgramsForMovie(movieId.toLong())
                    )
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

class MoviesViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    private val _allMovies = MutableStateFlow<List<Movie>>(emptyList())
    val allMovies: StateFlow<List<Movie>> = _allMovies.asStateFlow()

    init {
        viewModelScope.launch {
            moviesRepository.getAllMovies().collect { movies ->
                _allMovies.value = movies
            }
        }
    }
}

class MoviesViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoviesViewModel((application as LumiereApplication).container.moviesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




