package com.example.riseandroid.ui.screens.signup

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
import com.example.riseandroid.model.User
import com.example.riseandroid.repository.APIResource
import com.example.riseandroid.repository.IAuthRepo
import com.example.riseandroid.ui.screens.signup.validation.ValidateEmail
import com.example.riseandroid.ui.screens.signup.validation.ValidatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SignUpViewModel(val signUpCallback: (Credentials) -> Unit,
                      val authRepo: IAuthRepo,
                      private val validateEmail: ValidateEmail,
                      private val validatePassword: ValidatePassword
    ) : ViewModel() {

    private val TAG = "SignUpViewModel"
    private val _uiState = MutableStateFlow(SignUpState())
    val uiState: StateFlow<SignUpState> = _uiState.asStateFlow()

    private val _authResponse = MutableStateFlow(
        flow<APIResource<Credentials>>{APIResource.Success(null)}

    )

    val authResponse: StateFlow<APIResource<Credentials>> = _authResponse
        .flatMapLatest{it}
        .onEach { resource ->
            Log.i("FLOW", "Observed: ${resource.data}")
            if (resource is APIResource.Success &&resource.data!=null) {
                signUpCallback(resource.data)
                val user = User(resource.data.idToken)
                if (user != null) {
                    Log.i("SignUpViewModel", "Observed successful sign-up for user: $user")
                    flow{ emit(resource)}

                } else {
                    flow{ emit(APIResource.Error("User data is null",resource.data))}
                }
            }
            else if (resource is APIResource.Error<*>) {
                    Log.e("SignUpViewModel", "Observed error during sign-up: ${resource.message}")
                    _uiState.value = _uiState.value.copy(signUpError = true)
                    flow { emit(APIResource.Error(resource.message ?: "", resource.data))}
            }

        }
    .stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = APIResource.Success(null)
    )

    fun updateEmail(email: String) {
        resetError()
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updatePwd(password: String) {
        resetError()
        _uiState.value = _uiState.value.copy(password = password)
    }

    private fun resetError() {
        _uiState.value = _uiState.value.copy(signUpError = false)
    }

    fun onSubmit() {
        val emailResult = validateEmail.execute(_uiState.value.email)
        val passwordResult=validatePassword.execute(_uiState.value.password)
        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.successful }

        if (hasError) {
            _uiState.value = _uiState.value.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage

            )
            return
        }
        viewModelScope.launch {
            _authResponse.value = authRepo.signUp(
                email = uiState.value.email,
                password = uiState.value.password,
                connection = uiState.value.connection
            )
        }

    }

    companion object {
        private var Instance: SignUpViewModel? = null
        val SIGNUP_KEY = object : CreationExtras.Key<(Credentials) -> Unit> {}
        val APPLICATION_KEY = object : CreationExtras.Key<Application> {}
        val VALIDATE_EMAIL_KEY = object : CreationExtras.Key<ValidateEmail> {}
        val VALIDATE_PASSWORD_KEY = object : CreationExtras.Key<ValidatePassword> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val signUpCallback = this[SIGNUP_KEY] as (Credentials) -> Unit
                val application = this[APPLICATION_KEY] as LumiereApplication
                val validateEmail = this[VALIDATE_EMAIL_KEY] as ValidateEmail
                val validatePassword= this[VALIDATE_PASSWORD_KEY] as ValidatePassword


                if (Instance == null) {
                    val authRepo = application.container.authRepo
                    Instance = SignUpViewModel(signUpCallback = signUpCallback,
                        authRepo = authRepo,
                        validateEmail = validateEmail,
                        validatePassword=validatePassword)
                }
                Instance!!
            }
        }
    }
}
