package com.example.riseandroid.ui.screens.login

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.example.riseandroid.network.auth0.Auth0Api
import com.example.riseandroid.repository.APIResource
import com.example.riseandroid.ui.screens.signup.validation.ValidateEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForgotPasswordViewModel(
    private val authApi: Auth0Api,
    private val context: Context,
    auth0: Auth0
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    val emailError = mutableStateOf<String?>(null)
    private val validateEmail = ValidateEmail()

    fun sendForgotPasswordEmail(email: String) {
        emailError.value = null
        _message.value = null

        val validationResult = validateEmail.execute(email)
        if (!validationResult.successful) {
            emailError.value = validationResult.errorMessage
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val response = authApi.sendForgotPasswordEmail(email).execute()
                    if (response.isSuccessful) {
                        APIResource.Success(response.body())
                    } else {
                        APIResource.Error(response.message())
                    }
                } catch (e: Exception) {
                    APIResource.Error(e.message ?: "Unknown error")
                }
            }

            if (result is APIResource.Loading) {
                _isLoading.value = true
            }

            if (result is APIResource.Success) {
                _message.value = "Password reset email sent successfully!"
                _isLoading.value = false
            }

            if (result is APIResource.Error) {
                _message.value = result.message
                _isLoading.value = false
            }
        }
    }
}



class ForgotPasswordViewModelFactory(
    private val context: Context,
    private val account: Auth0
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            val authApi: Auth0Api = Retrofit.Builder()
                .baseUrl(Auth0Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Auth0Api::class.java)

            @Suppress("UNCHECKED_CAST")
            return ForgotPasswordViewModel(authApi, context, account) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


