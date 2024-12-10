package com.example.riseandroid.ui.screens.account

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.auth0.android.result.Credentials
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.data.entitys.MovieDao
import com.example.riseandroid.data.entitys.watchlist.UserManager
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.IAuthRepo
import com.example.riseandroid.repository.IWatchlistRepo
import com.example.riseandroid.repository.MovieRepo
import com.example.riseandroid.ui.screens.signup.SignUpState
import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModel
import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepo: IAuthRepo,
    private val watchlistRepo: IWatchlistRepo,
    private val userManager: UserManager,
    private val application: Application,
    private val movieRepo: MovieRepo,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> get() = _authState

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> get() = _signUpState

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> get() = _email

    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> get() = _authToken
    private val _watchlistViewModel: WatchlistViewModel by lazy {
        WatchlistViewModelFactory(
            watchlistRepo, userManager, movieRepo, application
        ).create(WatchlistViewModel::class.java)
    }

    val watchlistViewModel: WatchlistViewModel get() = _watchlistViewModel

    fun setAuthenticated(credentials: Credentials) {
        _authState.value = AuthState.Authenticated(credentials)
        _email.value = credentials.user.email
        _authToken.value=credentials.accessToken

        val userId = getUserIdFromCredentials(credentials)
        userManager.setUserId(userId)
    }


    private fun getUserIdFromCredentials(credentials: Credentials): Int {
        return credentials.user.getId()?.split("|")?.last()?.toIntOrNull() ?: 0
    }

    private fun getUserId(): Int {
        val currentState = authState.value
        return if (currentState is AuthState.Authenticated) {
            getUserIdFromCredentials(currentState.credentials)
        } else {
            0
        }
    }

    fun getAccessToken(): String? {
        val currentState = authState.value
        return if (currentState is AuthState.Authenticated) {
            currentState.credentials.accessToken
        } else {
            null
        }
    }

    fun resetSignUpState() {
        _signUpState.value = SignUpState()
    }


    fun logout() {
        _authState.value = AuthState.Unauthenticated
        viewModelScope.launch {
            authRepo.logout()
            userManager.clearUserId()
        }
    }

    // Fetch the auth token
    private fun fetchAuthToken() {
        viewModelScope.launch {
            try {
                val credentialsFlow = authRepo.getCredentials()
                val credentialsResource = credentialsFlow.firstOrNull()

                if (credentialsResource is ApiResource.Success) {
                    val token = credentialsResource.data?.accessToken
                    if (token != null) {
                        _authToken.value = token
                        setAuthenticated(credentialsResource.data)
                    }
                } else {
                    _authToken.value = null
                    _authState.value = AuthState.Unauthenticated
                }
            } catch (e: Exception) {
                _authToken.value = null
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    companion object {
        val APPLICATION_KEY = object : CreationExtras.Key<Application> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as? LumiereApplication
                    ?: throw IllegalStateException("Application not found in CreationExtras")
                val authRepo = application.container.authRepo
                    ?: throw IllegalStateException("AuthRepo not found")
                val userManager = application.userManager
                val watchlistRepo = application.container.watchlistRepo
                    ?: throw IllegalStateException("WatchlistRepo not found")
                val movieRepo = application.container.movieRepo

                AuthViewModel(
                    authRepo = authRepo, userManager = userManager,
                    watchlistRepo = watchlistRepo,
                    movieRepo = movieRepo,
                    application = application
                )
            }
        }
    }
}

sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val credentials: Credentials) : AuthState()
}