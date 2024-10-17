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
import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.ui.screens.homepage.HomepageUiState
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.CountDownLatch


class FakeHomepageViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    var homepageUiState: HomepageUiState by mutableStateOf(HomepageUiState.Loading)
        private set

    private final val countown : CountDownLatch = CountDownLatch(1)

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading
            countown.countDown()
            homepageUiState = try {
                val recentMovieListResult =  moviesRepository.getRecentMovies()
                val nonRecentMovieListResult = moviesRepository.getNonRecentMovies()
                HomepageUiState.Succes(recentMovies = recentMovieListResult, nonRecentMovies = nonRecentMovieListResult)
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
                val moviesRepository = application.container.moviesRepository
                HomepageViewModel(moviesRepository = moviesRepository)
            }
        }
    }
}