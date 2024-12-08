package com.example.riseandroid.ui.screens.homepage

import android.util.Log
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
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.repository.IEventRepo
import com.example.riseandroid.repository.IMoviePosterRepo
import com.example.riseandroid.repository.IMovieRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed interface HomepageUiState {
    data class Succes(val allMovies: StateFlow<List<MovieModel>>,
                      val recentMovies: StateFlow<List<MoviePoster>>,
                      val events: StateFlow<List<EventModel>>) : HomepageUiState
    object Error : HomepageUiState
    object Loading : HomepageUiState
}

class HomepageViewModel(
    private val movieRepo: IMovieRepo,
    private val moviePosterRepo: IMoviePosterRepo,
    private val eventRepo: IEventRepo
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
    private val _selectedCinema = MutableStateFlow("Brugge")
    val selectedCinema: StateFlow<String> = _selectedCinema.asStateFlow()

    private val _events = MutableStateFlow<List<EventModel>>(emptyList())
    val events = _events.asStateFlow()

    private val _filteredEvents = MutableStateFlow<List<EventModel>>(emptyList())
    val filteredEvents: StateFlow<List<EventModel>> = _filteredEvents.asStateFlow()


    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    init {
        getAllMoviesList()
        getNonRecentMovieList()
        getEvents()
    }

    private fun getAllMoviesList() {
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading

            val cinemas = listOf("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")

            movieRepo.getAllMoviesList(getCurrentDate(), cinemas,"")
                .collect { movies ->
                    _allMovies.value = movies
                    homepageUiState = HomepageUiState.Succes(
                        allMovies = _allMovies,
                        recentMovies = recentMovies,
                        events = events,
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
                        events = events,
                    )

                }
        }
    }


    private fun getEvents() {
        viewModelScope.launch {
            try {
                homepageUiState = HomepageUiState.Loading
                eventRepo.refreshEvents()
                eventRepo.getAllEventsList().collect { eventsList ->
                    _events.value = eventsList
                    _filteredEvents.value = eventsList.filter { event ->
                        var cinema = event.cinemas ?: emptyList()
                        cinema.any { it.name.equals("Brugge", ignoreCase = true) }
                    }
                    homepageUiState = HomepageUiState.Succes(
                        allMovies = allMovies,
                        recentMovies = recentMovies,
                        events = events
                    )
                }
            } catch (e: Exception) {
                homepageUiState = HomepageUiState.Error
                Log.e("HomepageViewModel", "Error fetching events", e)
            }
        }
    }


    fun filterEventsByCinema(selectedCinema: String) {
        viewModelScope.launch {
            val filtered = _events.value.filter { event ->
                var cinema = event.cinemas ?: emptyList()
                cinema.any { it.name.equals(selectedCinema, ignoreCase = true) }
            }
            _filteredEvents.value = filtered
        }
    }

    fun updateSelectedCinema(newCinema: String) {
        _selectedCinema.value = newCinema
        filterEventsByCinema(newCinema)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                val movieRepo=application.container.movieRepo
                val moviePosterRepo = application.container.moviePosterRepo
                val eventRepo = application.container.eventRepo
                HomepageViewModel(
                    movieRepo = movieRepo,
                    moviePosterRepo = moviePosterRepo,
                    eventRepo = eventRepo
                )
            }
        }
    }
}