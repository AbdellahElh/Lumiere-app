package com.example.riseandroid.ui.screens.watchlist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class WatchlistViewModel : ViewModel() {
    private val _watchlist = MutableStateFlow<Set<Long>>(emptySet())
    val watchlist: StateFlow<Set<Long>> = _watchlist

    fun toggleMovieInWatchlist(movieId: Long) {
        _watchlist.update { currentList ->
            if (currentList.contains(movieId)) {
                currentList - movieId
            } else {
                currentList + movieId
            }
        }
    }


    fun isInWatchlist(movieId: Long): Boolean {
        return _watchlist.value.contains(movieId)
    }
}

