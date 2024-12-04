package com.example.riseandroid.ui.screens.watchlist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class WatchlistViewModel : ViewModel() {
    private val _watchlist = MutableStateFlow<Set<Int>>(emptySet())
    val watchlist: StateFlow<Set<Int>> = _watchlist

    fun toggleMovieInWatchlist(movieId: Int) {
        _watchlist.update { currentList ->
            if (currentList.contains(movieId)) {
                currentList - movieId
            } else {
                currentList + movieId
            }
        }
    }


    fun isInWatchlist(movieId: Int): Boolean {
        return _watchlist.value.contains(movieId)
    }
}

