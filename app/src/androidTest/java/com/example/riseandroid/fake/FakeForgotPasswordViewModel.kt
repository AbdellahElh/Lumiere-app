package com.example.riseandroid.fake

import android.app.Application
import com.auth0.android.Auth0
import com.example.riseandroid.data.entitys.watchlist.UserManager
import com.example.riseandroid.repository.IAuthRepo
import com.example.riseandroid.repository.IMovieRepo
import com.example.riseandroid.repository.IWatchlistRepo
import com.example.riseandroid.ui.screens.login.ForgotPasswordViewModel

class FakeForgotPasswordViewModel : ForgotPasswordViewModel(
    authApi = FakeAuth0Api(),
    context = FakeApplication(),
    auth0 = Auth0("clientId", "domain")
)

class FakeApplication : Application() {

    val userManager: UserManager = FakeUserManager()
    val fakeAuthRepo: IAuthRepo = FakeAuthRepo()
    val fakeWatchlistRepo: IWatchlistRepo = FakeWatchlistRepo()
    val fakeMovieRepo: IMovieRepo = FakeMovieRepo()

    val container = object {
        val authRepo: IAuthRepo = fakeAuthRepo
        val watchlistRepo: IWatchlistRepo = fakeWatchlistRepo
        val movieRepo: IMovieRepo = fakeMovieRepo
    }

}