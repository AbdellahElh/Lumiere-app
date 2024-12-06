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
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.repository.IMovieRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface MovieDetailUiState {
    data class Success(val specificMovie: MovieModel) : MovieDetailUiState
    object Error : MovieDetailUiState
    object Loading : MovieDetailUiState
}

class MovieDetailViewModel(
    private val movieId: Int,
    private val movieRepo: IMovieRepo,
) : ViewModel() {

    var movieDetailUiState: MovieDetailUiState by mutableStateOf(MovieDetailUiState.Loading)
        private set

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

                if (specificMovie != null) {
                    _selectedMovie.value = specificMovie
                    movieDetailUiState = MovieDetailUiState.Success(specificMovie)
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
                MovieDetailViewModel(movieId, movieRepo)
            }
        }
    }
}






