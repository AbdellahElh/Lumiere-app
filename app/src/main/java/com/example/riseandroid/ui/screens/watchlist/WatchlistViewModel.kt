package com.example.riseandroid.ui.screens.watchlist

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.riseandroid.data.entitys.watchlist.UserManager
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.repository.IWatchlistRepo
import com.example.riseandroid.util.isNetworkAvailable
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

open class WatchlistViewModel(
    private val watchlistRepo: IWatchlistRepo,
    private val userManager: UserManager,
    private val context: Context
) : ViewModel() {

    private val _watchlist = MutableStateFlow<List<MovieModel>>(emptyList())
    val watchlist: StateFlow<List<MovieModel>> = _watchlist.asStateFlow()

    private val _eventFlow = MutableSharedFlow<WatchlistEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    init {
        userManager.currentUserId
            .onEach { userId ->
                if (userId != null) {
                    if (isNetworkAvailable(context)) {
                        syncWatchlist()
                    } else {
                        loadOfflineWatchlist(userId)
                    }
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
            }
            .onEach { movies ->
                _watchlist.value = movies
            }
            .launchIn(viewModelScope)
    }

    fun refreshWatchlist() {
        val userId = userManager.currentUserId.value
        if (userId != null) {
            if (isNetworkAvailable(context)) {
                syncWatchlist()
            } else {
                loadOfflineWatchlist(userId)
            }
        }
    }


    fun syncWatchlist() {
        val userId = userManager.currentUserId.value
        if (userId != null && isNetworkAvailable(context)) {
            viewModelScope.launch {
                _isSyncing.value = true
                try {
                    watchlistRepo.syncWatchlistWithBackend(userId)
                    Log.d("WatchlistViewModel", "Watchlist gesynchroniseerd")
                } catch (e: Exception) {
                    Log.e("WatchlistViewModel", "Fout bij synchroniseren van watchlist: ${e.message}")
                } finally {
                    _isSyncing.value = false // Synchronisatie eindigt
                }
            }
        }
    }


    private fun loadOfflineWatchlist(userId: Int) {
        viewModelScope.launch {
            try {
                Log.d("WatchlistViewModel", "Offline watchlist geladen voor user $userId")
                // Hier kun je meer logica toevoegen als caching belangrijk is.
            } catch (e: Exception) {
                Log.e("WatchlistViewModel", "Fout bij offline laden: ${e.message}", e)
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
                    try {
                        watchlistRepo.addToWatchlist(movie, userId)
                    } catch (e: HttpException) {
                        if (e.code() == 409) {
                            _eventFlow.emit(WatchlistEvent.ShowToast("Deze film staat al in je watchlist"))
                        } else {
                            throw e
                        }
                    }
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
    private val userManager: UserManager,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchlistViewModel::class.java)) {
            return WatchlistViewModel(
                watchlistRepo, userManager, application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




