package com.example.riseandroid.fake

import android.app.Application
import com.auth0.android.Auth0
import com.example.riseandroid.ui.screens.account.AuthViewModel
import com.example.riseandroid.ui.screens.login.ForgotPasswordViewModel

class FakeAuthViewModel : AuthViewModel(
    authRepo = FakeAuthRepo(),
    watchlistRepo = FakeWatchlistRepo(),
    userManager = FakeUserManager(),
    application = Application(),
    movieRepo = FakeMovieRepo()
)