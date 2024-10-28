package com.example.riseandroid.ui.screens.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.auth0.android.result.Credentials
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.repository.APIResource
import com.example.riseandroid.repository.IAuthRepo
import com.example.riseandroid.ui.screens.signUp.AuthViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    val login: (Credentials) -> Unit,
    val authRepo: IAuthRepo,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private val _authResponse = MutableStateFlow<APIResource<Credentials>>(APIResource.Initial())
    val authResponse: StateFlow<APIResource<Credentials>> = _authResponse

    // We voegen een nieuwe flow toe om de navigatie te beheren
    private val _navigateToAccount = MutableSharedFlow<Boolean>()
    val navigateToAccount: SharedFlow<Boolean> = _navigateToAccount

    fun resetAuthResponse() {
        _authResponse.value = APIResource.Initial()
    }

    fun updateUserName(name: String) {
        resetError()
        _uiState.value = _uiState.value.copy(username = name)
    }

    fun updatePwd(pwd: String) {
        resetError()
        _uiState.value = _uiState.value.copy(password = pwd)
    }

    private fun resetError() {
        _uiState.value = _uiState.value.copy(loginError = false)
    }

    fun onSubmit() {
        viewModelScope.launch {
            authRepo.getCredentials(userName = uiState.value.username, password = uiState.value.password)
                .collect { apiResource ->
                    _authResponse.value = apiResource

                    if (apiResource is APIResource.Success && apiResource.data != null) {
                        // Credentials ontvangen, update de authState
                        authViewModel.setAuthenticated(apiResource.data) // Update de AuthViewModel
                        login(apiResource.data) // Roep de login callback aan

                        // Navigeren naar het accountscherm
                        _navigateToAccount.emit(true) // Emitteer navigatie
                    } else if (apiResource is APIResource.Error<*>) {
                        _uiState.value = _uiState.value.copy(loginError = true)
                    }
                }
        }
    }

    companion object {
        private var Instance: LoginViewModel? = null
        val LOGIN_KEY = object : CreationExtras.Key<(Credentials) -> Unit> {}
        val APPLICATION_KEY = object : CreationExtras.Key<Application> {}
        val AUTH_VIEWMODEL_KEY = object : CreationExtras.Key<AuthViewModel> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val login = this[LOGIN_KEY] as (Credentials) -> Unit
                val application = this[APPLICATION_KEY] as Application
                val authViewModel = this[AUTH_VIEWMODEL_KEY] as AuthViewModel
                if (Instance == null) {
                    val authRepo = (application as LumiereApplication).container.authRepo
                    Instance = LoginViewModel(login = login, authRepo = authRepo, authViewModel = authViewModel)
                }
                Instance!!
            }
        }
    }
}

