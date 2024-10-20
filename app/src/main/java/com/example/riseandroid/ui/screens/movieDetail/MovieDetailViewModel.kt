package com.example.riseandroid.ui.screens.movieDetail

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.model.Movie
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface MovieDetailUiState {
    data class Success(val specificMovie: Movie) : MovieDetailUiState
    object Error : MovieDetailUiState
    object Loading : MovieDetailUiState
}

class MovieDetailViewModel(
    private val movieId: Long,
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    var movieDetailUiState: MovieDetailUiState by mutableStateOf(MovieDetailUiState.Loading)
        private set

    init {
        getMovie()
    }

    private fun getMovie() {
        viewModelScope.launch {
            movieDetailUiState = MovieDetailUiState.Loading
            movieDetailUiState = try {
                val specificMovie = moviesRepository.getSpecificMovie(movieId)
                if (specificMovie != null) {
                    MovieDetailUiState.Success(specificMovie)
                } else {
                    MovieDetailUiState.Error
                }
            } catch (e: IOException) {
                MovieDetailUiState.Error
            } catch (e: HttpException) {
                MovieDetailUiState.Error
            }
        }
    }

    companion object {
        fun provideFactory(movieId: Long): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                val moviesRepository = application.container.moviesRepository
                MovieDetailViewModel(movieId, moviesRepository)
            }
        }
    }
}

