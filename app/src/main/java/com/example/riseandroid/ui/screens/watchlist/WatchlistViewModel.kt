package com.example.riseandroid.ui.screens.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.repository.IWatchlistRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WatchlistViewModel(private val watchlistRepo: IWatchlistRepo, private val userId: Int) : ViewModel() {

    private val _watchlist = MutableStateFlow<Set<Int>>(emptySet())
    val watchlist: StateFlow<Set<Int>> = _watchlist

    init {
        loadWatchlist()
    }

    private fun loadWatchlist() {
        viewModelScope.launch {
            watchlistRepo.getMoviesInWatchlist(userId).collect { movies ->
                _watchlist.value = movies.map { it.id }.toSet()
            }
        }
    }

    fun toggleMovieInWatchlist(movie: MovieModel) {
        viewModelScope.launch {
            if (isInWatchlist(movie.id)) {
                watchlistRepo.removeFromWatchlist(movie.id, userId)
                _watchlist.value = _watchlist.value - movie.id
            } else {
                watchlistRepo.addToWatchlist(movie, userId)
                _watchlist.value = _watchlist.value + movie.id
            }
        }
    }

    fun isInWatchlist(movieId: Int): Boolean {
        return _watchlist.value.contains(movieId)
    }
}

class WatchlistViewModelFactory(
    private val watchlistRepo: IWatchlistRepo,
    private val userId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchlistViewModel::class.java)) {
            return WatchlistViewModel(watchlistRepo, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
