package com.example.riseandroid.ui.screens.account

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var username = mutableStateOf("")
    var password = mutableStateOf("")
    var passwordVisible = mutableStateOf(false)

    fun onUsernameChange(newUsername: String) {
        username.value = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun onPasswordVisibilityChange() {
        passwordVisible.value = !passwordVisible.value
    }

    fun login() {
        println("Login with username: ${username.value} and password: ${password.value}")
    }
}

