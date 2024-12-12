package com.example.riseandroid.fake

import com.example.riseandroid.mockdata.MovieListMock
import com.example.riseandroid.network.ResponseMovie
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailUiState
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeMovieDetailViewModel(private val forcedState: MovieDetailUiState) : MovieDetailViewModel(0, FakeMovieRepo()) {
    override var movieDetailUiState: MovieDetailUiState = forcedState
    private val movie = MovieListMock().loadResponseMoviesMock().first()
    private val _selectedMovie = MutableStateFlow((forcedState as? MovieDetailUiState.Success)?.specificMovie ?: movie)
    override val selectedMovie: StateFlow<ResponseMovie> = _selectedMovie
}
