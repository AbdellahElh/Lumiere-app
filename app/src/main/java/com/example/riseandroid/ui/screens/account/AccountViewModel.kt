package com.example.riseandroid.ui.screens.account

import androidx.lifecycle.ViewModel
import com.auth0.android.result.Credentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AccountViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> get() = _authState

    fun setAuthenticated(credentials: Credentials) {
        _authState.value = AuthState.Authenticated(credentials)
    }

    fun logout() {
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val credentials: Credentials) : AuthState()
}
