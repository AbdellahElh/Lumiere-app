package com.example.riseandroid.fake

import android.content.Context
import com.example.riseandroid.data.entitys.watchlist.UserManager
import com.example.riseandroid.repository.IWatchlistRepo
import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModel

class FakeWatchlistViewModel(
    watchlistRepo: IWatchlistRepo,
    userManager: UserManager,
    context: Context
) : WatchlistViewModel(watchlistRepo, userManager, context)


