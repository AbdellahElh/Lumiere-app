package com.example.riseandroid.ui.screens.signup

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val connection: String = "Username-Password-Authentication",
    val signUpError: Boolean = false,
    val isLoading: Boolean = false,

    val emailError:String?=null,
    val passwordError:String?=null,
    val confirmEmailError:String?=null
)