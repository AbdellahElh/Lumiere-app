package com.example.riseandroid.fake

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.data.lumiere.ProgramRepository
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.ui.screens.homepage.HomepageUiState
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class FakeHomepageViewModel(
    private val recentMovies: List<MoviePoster> = emptyList(),
    private val allMovies: List<MovieModel> = emptyList(),
    private val events: List<EventModel> = emptyList(),
    private val programRepository: ProgramRepository
) : ViewModel() {
    var homepageUiState: HomepageUiState by mutableStateOf(HomepageUiState.Loading)
        private set

    private val _recentMovies = MutableStateFlow(recentMovies)
    val recentMoviesFlow = _recentMovies.asStateFlow()

    private val _allMovies = MutableStateFlow(allMovies)
    val allMoviesFlow = _allMovies.asStateFlow()

    private val _events = MutableStateFlow(events)
    val eventsFlow = _events.asStateFlow()

    init {
        homepageUiState = HomepageUiState.Succes(
            allMovies = allMoviesFlow,
            recentMovies = recentMoviesFlow,
            events = eventsFlow
        )
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                val moviePosterRepo = application.container.moviePosterRepo
                val eventRepo = application.container.eventRepo
                HomepageViewModel(
                    application.container.movieRepo,
                    moviePosterRepo = moviePosterRepo,
                    eventRepo = eventRepo
                )
            }
        }
    }
}
