package com.example.riseandroid.ui.screens.login

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
import com.example.riseandroid.ui.screens.account.AuthViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(
    val login: (Credentials) -> Unit,
    val authRepo: IAuthRepo,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private val _authResponse = MutableStateFlow<ApiResource<Credentials>>(ApiResource.Initial())
  //  val authResponse: StateFlow<ApiResource<Credentials>> = _authResponse

    // We voegen een nieuwe flow toe om de navigatie te beheren
    private val _navigateToAccount = MutableSharedFlow<Boolean>()
    val navigateToAccount: SharedFlow<Boolean> = _navigateToAccount

//    fun resetAuthResponse() {
//        _authResponse.value = ApiResource.Initial()
//    }

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

    val authResponse: StateFlow<ApiResource<Credentials>> = _authResponse
        .flatMapLatest { resource ->
            flow {
                Log.i("LoginViewModel", "Observed: ${resource.data}")


                if (resource is ApiResource.Success && resource.data != null) {

                    login(resource.data)

                }

                else if (resource is ApiResource.Error<*>) {
                    Log.e("LoginViewModel", "Error during login: ${resource.message}")
                    _uiState.value = _uiState.value.copy(loginError = true)
                    emit(ApiResource.Error(resource.message ?: "Unknown error", resource.data))
                }

                else {
                    emit(resource)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ApiResource.Success(null)
        )


    fun onSubmit() {
        viewModelScope.launch {
            authRepo.performLogin(userName = uiState.value.username, password = uiState.value.password)
                .collect { apiResource ->
                    _authResponse.value = apiResource
                    if (apiResource is ApiResource.Success && apiResource.data != null) {
                        // Credentials ontvangen, update de authState
                        authViewModel.setAuthenticated(apiResource.data)
                        login(apiResource.data)

                        // Navigeren naar het accountscherm
                        _navigateToAccount.emit(true)
                    } else if (apiResource is ApiResource.Error<*>) {
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

