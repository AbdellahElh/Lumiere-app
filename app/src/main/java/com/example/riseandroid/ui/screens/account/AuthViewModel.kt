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
import com.example.riseandroid.repository.IAuthRepo
import com.example.riseandroid.ui.screens.signup.SignUpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(val authRepo: IAuthRepo) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> get() = _authState

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> get() = _signUpState

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> get() = _email


    fun setAuthenticated(credentials: Credentials) {
        _authState.value = AuthState.Authenticated(credentials)
        _email.value = credentials.user.email
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

                AuthViewModel(authRepo = authRepo)
            }
        }
    }

}

sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val credentials: Credentials) : AuthState()
}
