package com.example.riseandroid.ui.screens.watchlist

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.riseandroid.data.entitys.watchlist.UserManager
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.repository.IWatchlistRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WatchlistViewModel(private val watchlistRepo: IWatchlistRepo, private val userManager: UserManager) : ViewModel() {

    private val _watchlist = MutableStateFlow<List<MovieModel>>(emptyList())
    val watchlist: StateFlow<List<MovieModel>> = _watchlist.asStateFlow()

    private val _eventFlow = MutableSharedFlow<WatchlistEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        userManager.currentUserId
            .onEach { userId ->
                if (userId != null) {
                    syncWatchlist(userId)
                } else {
                    _watchlist.value = emptyList()
                }
            }
            .flatMapLatest { userId ->
                if (userId != null) {
                    watchlistRepo.getMoviesInWatchlist(userId)
                } else {
                    flowOf(emptyList())
                }
            }.onEach { movies ->
                _watchlist.value = movies
            }.launchIn(viewModelScope)
    }

    fun refreshWatchlist() {
        val userId = userManager.currentUserId.value
        if (userId != null) {
            syncWatchlist(userId)
        }
    }


    private fun syncWatchlist(userId: Int) {
        viewModelScope.launch {
            try {
                watchlistRepo.syncWatchlistWithBackend(userId)
                Log.d("WatchlistViewModel", "Watchlist gesynchroniseerd voor user $userId")
            } catch (e: Exception) {
                Log.e("WatchlistViewModel", "Fout bij synchroniseren: ${e.message}", e)
            }
        }
    }


    @SuppressLint("NewApi")
    fun toggleMovieInWatchlist(movie: MovieModel) {
        viewModelScope.launch {
            val userId = userManager.currentUserId.value
            if (userId != null) {
                if (isInWatchlist(movie.id)) {
                    try {
                        watchlistRepo.removeFromWatchlist(movie.id, userId)
                    } catch (e: HttpException) {
                        if (e.code() == 404) {
                            _eventFlow.emit(WatchlistEvent.ShowToast("Film werd al verwijderd uit de watchlist"))
                        } else {
                            throw e
                        }
                    }
                } else {
                    watchlistRepo.addToWatchlist(movie, userId)
                }
            }
        }
    }


    fun isInWatchlist(movieId: Int): Boolean {
        return _watchlist.value.any { it.id == movieId }
    }

    sealed class WatchlistEvent {
        data class ShowToast(val message: String) : WatchlistEvent()
    }

}


class WatchlistViewModelFactory(
    private val watchlistRepo: IWatchlistRepo,
    private val userManager: UserManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchlistViewModel::class.java)) {
            return WatchlistViewModel(watchlistRepo, userManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



