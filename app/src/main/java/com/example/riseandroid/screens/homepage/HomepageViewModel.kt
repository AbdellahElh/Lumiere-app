package com.example.riseandroid.screens.homepage

import android.net.http.HttpException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.riseandroid.data.lumière.MoviesRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface HomepageUiState {
    data class Succes(val movies: String) : HomepageUiState
    object Error : HomepageUiState
    object Loading : HomepageUiState
}

class HomepageViewModel(private val moviesRepository : MoviesRepository) : ViewModel() {
    var homepageUiState : HomepageUiState by mutableStateOf(HomepageUiState.Loading)
        private set

    init {
        getMovies()
    }

    fun getRecentMovies() {
       viewModelScope.launch {
           homepageUiState = HomepageUiState.Loading
           homepageUiState = try {
               val movieListResult = moviesRepository.getRecentMovies()
               HomepageUiState.Succes(
                   "Success: ${movieListResult.size} movies retrieved"
               )
           } catch (e : IOException) {
               HomepageUiState.Error
           } catch (e : HttpException) {
               HomepageUiState.Error
           }
       }
    }

    fun getNonRecentMovies() {
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading
            homepageUiState = try {
                val movieListResult = moviesRepository.getNonRecentMovies()
                HomepageUiState.Succes(
                    "Success: ${movieListResult.size} movies retrieved"
                )
            } catch (e : IOException) {
                HomepageUiState.Error
            } catch (e : HttpException) {
                HomepageUiState.Error
            }
        }
    }

//    companion object {
//        val Factory : ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[APPLICATION_KEY] as HomepageApplication)
//                val moviesRepository = application.container.moviesRepository
//                HomepageViewModel(moviesRepository = moviesRepository)
//            }
//        }
//    }
}