package com.example.riseandroid.ui.screens.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.auth0.android.result.Credentials
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.IAuthRepo
import com.example.riseandroid.ui.screens.signup.SignUpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AuthViewModel(val authRepo: IAuthRepo) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> get() = _authState

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> get() = _signUpState

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> get() = _email

    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> get() = _authToken



    // Set authenticated state
    fun setAuthenticated(credentials: Credentials) {
        _authState.value = AuthState.Authenticated(credentials)
        _email.value = credentials.user.email
        _authToken.value=credentials.accessToken
    }

    // Reset signup state
    fun resetSignUpState() {
        _signUpState.value = SignUpState()
    }

    // Logout functionality
    fun logout() {
        _authState.value = AuthState.Unauthenticated
        viewModelScope.launch {
            authRepo.logout()
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

                AuthViewModel(authRepo = authRepo)
            }
        }
    }
}

sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val credentials: Credentials) : AuthState()
}
