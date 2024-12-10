package com.example.riseandroid.fake

import android.content.Context
import com.example.riseandroid.data.entitys.watchlist.UserManager
import com.example.riseandroid.repository.IMovieRepo
import com.example.riseandroid.repository.IWatchlistRepo
import com.example.riseandroid.repository.MovieRepo
import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModel

class FakeWatchlistViewModel(
    watchlistRepo: FakeWatchlistRepo,
    movieRepo: FakeMovieRepo,
    userManager: FakeUserManager,
    context: Context
) : WatchlistViewModel(watchlistRepo,movieRepo, userManager, context)


