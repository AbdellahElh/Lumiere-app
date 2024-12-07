package com.example.riseandroid.ui.screens.movieProgram

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
import com.example.riseandroid.util.getTodayDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException



sealed interface ProgramUiState {
    data class Succes(val movieList: StateFlow<List<MovieModel>>) : ProgramUiState
    object Error : ProgramUiState
    object Loading : ProgramUiState
}


class MovieProgramViewModel(
    private val movieRepo: IMovieRepo
) : ViewModel() {

    var programUiState: ProgramUiState by mutableStateOf(ProgramUiState.Loading)
        private set

    private val _movieList = MutableStateFlow<List<MovieModel>>(emptyList())
    val movieList = _movieList.asStateFlow()

    private val _selectedDate = MutableStateFlow(getTodayDate())
    val selectedDate= _selectedDate.asStateFlow()

    private val _searchTitle = MutableStateFlow<String?>(null)
    val searchTitle: StateFlow<String?> = _searchTitle.asStateFlow()

    private val _selectedCinemas = MutableStateFlow<List<String>>(emptyList())
    val selectedCinemas= _selectedCinemas.asStateFlow()

    fun updateFilters(date: String, cinemas: List<String>,searchTitle:String) {
        _selectedDate.value = date
        _selectedCinemas.value = cinemas
        _searchTitle.value=searchTitle
    }


    fun applyFilters() {
        viewModelScope.launch {
            programUiState = ProgramUiState.Loading
            try {
                getAllMoviesList()
            } catch (e: IOException) {
                programUiState = ProgramUiState.Error
            } catch (e: HttpException) {
                programUiState = ProgramUiState.Error
            }
        }
    }

    init{
        getAllMoviesList()
    }

    private fun getAllMoviesList() {
        viewModelScope.launch {
            programUiState = ProgramUiState.Loading

            val cinemas = if (selectedCinemas.value.isEmpty()) {
                listOf("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")
            } else {
                selectedCinemas.value
            }

            movieRepo.getAllMoviesList(selectedDate.value, cinemas,searchTitle.value)
                .collect { movies ->
                    _movieList.value = movies
                    programUiState = ProgramUiState.Succes(
                        movieList = _movieList,)
                }
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                val movieRepo=application.container.movieRepo
                MovieProgramViewModel(movieRepo=movieRepo)
            }
        }
    }

}