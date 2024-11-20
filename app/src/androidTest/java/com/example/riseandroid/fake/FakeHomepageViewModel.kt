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
import com.example.riseandroid.ui.screens.homepage.HomepageUiState
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.CountDownLatch
import com.example.riseandroid.data.lumiere.ProgramRepository
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.Program
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest


class FakeHomepageViewModel(
    private val programRepository: ProgramRepository,
) : ViewModel() {
    var homepageUiState: HomepageUiState by mutableStateOf(HomepageUiState.Loading)
        private set
    private val _recentMovies = MutableStateFlow<List<Program>>(emptyList())
    val recentMovies = _recentMovies.asStateFlow()

    private val _nonRecentMovies = MutableStateFlow<List<Program>>(emptyList())
    val nonRecentMovies = _nonRecentMovies.asStateFlow()

    private val _programFilms = MutableStateFlow<List<Program>>(emptyList())
    val programFilms = _programFilms.asStateFlow()

    private val _allMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val allMovies = _allMovies.asStateFlow()

    private final val countown : CountDownLatch = CountDownLatch(1)


    var moviesLocation: String by mutableStateOf("Brugge")
        private set
    init {
        getMovies()
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
                    _nonRecentMovies.value = it
                }
                HomepageUiState.Succes(
                    recentMovies = recentMovies,
                    allMovies = allMovies,
                    programFilms = programFilms
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
                countown.countDown()

                programFilms.collectLatest { _programFilms.value = it }
                movieList.collectLatest {
                    _recentMovies.value = it
                    _nonRecentMovies.value = it
                }
                HomepageUiState.Succes(
                    recentMovies = recentMovies,
                    allMovies = allMovies,
                    programFilms = programFilms
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
                HomepageViewModel(programRepository = programRepository,application.container.movieRepo)
            }
        }
    }
}
