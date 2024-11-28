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
import com.example.riseandroid.data.lumiere.ProgramRepository
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.Program
import com.example.riseandroid.repository.IEventRepo
import com.example.riseandroid.repository.IMovieRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed interface HomepageUiState {
    data class Succes(val allMovies: StateFlow<List<MovieModel>>,
                      val recentMovies: StateFlow<List<Program>>,
                      val programFilms: Flow<List<Program>>,
                      val events: StateFlow<List<EventModel>>) : HomepageUiState
    object Error : HomepageUiState
    object Loading : HomepageUiState
}

class HomepageViewModel(
    private val programRepository: ProgramRepository,
    private val movieRepo: IMovieRepo,
    private val eventRepo: IEventRepo
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

    private val _selectedMovie = MutableStateFlow<MovieModel?>(null)
    val selectedMovie: StateFlow<MovieModel?> = _selectedMovie.asStateFlow()

    fun fetchMovieById(id: Int) {
        viewModelScope.launch {
            try {
                val movie = movieRepo.getSpecificMovie(id)
                _selectedMovie.value = movie
            } catch (e: Exception) {
                _selectedMovie.value = null
            }
        }
    }


    private val _events = MutableStateFlow<List<EventModel>>(emptyList())
    val events = _events.asStateFlow()

    private val _filteredEvents = MutableStateFlow<List<EventModel>>(emptyList())
    val filteredEvents: StateFlow<List<EventModel>> = _filteredEvents.asStateFlow()

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
        getEvents()
    }

    private fun getEvents() {
        viewModelScope.launch {
            try {
                homepageUiState = HomepageUiState.Loading
                eventRepo.refreshEvents()
                eventRepo.getAllEventsList()
                    .collect { eventsList ->
                        _events.value = eventsList

                        // Stel de standaard gefilterde events in op "Brugge"
                        val defaultCinema = "Brugge"
                        _filteredEvents.value = eventsList.filter { event ->
                            event.cinemas.any { it.name.equals(defaultCinema, ignoreCase = true) }
                        }

                        homepageUiState = HomepageUiState.Succes(
                            allMovies = allMovies,
                            recentMovies = recentMovies,
                            programFilms = programFilms,
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
                event.cinemas.any { it.name.equals(selectedCinema, ignoreCase = true) }
            }
            _filteredEvents.value = filtered
        }
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
                            events = events,
                        )

                }
        }
    }

    fun applyFilters() {
        viewModelScope.launch {
            homepageUiState = HomepageUiState.Loading
            try {
                getAllMoviesList()
                getEvents()
                homepageUiState = HomepageUiState.Succes(
                    recentMovies = recentMovies,
                    programFilms = programFilms,
                    allMovies = allMovies,
                    events = events
                )
            } catch (e: IOException) {
                homepageUiState = HomepageUiState.Error
            } catch (e: HttpException) {
                homepageUiState = HomepageUiState.Error
            }
        }
    }


    private fun getEventsByLocation() {
        viewModelScope.launch {
            try {
                getEvents()
            } catch (e: Exception) {
                Log.e("HomepageViewModel", "Error fetching events by location", e)
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
                    allMovies = allMovies,
                    events = events
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
            getEventsByLocation()
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
                    allMovies = allMovies,
                    events = events
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
                val eventRepo=application.container.eventRepo
                HomepageViewModel(
                    programRepository = programRepository, movieRepo = movieRepo,
                    eventRepo = eventRepo
                )
            }
        }
    }
}