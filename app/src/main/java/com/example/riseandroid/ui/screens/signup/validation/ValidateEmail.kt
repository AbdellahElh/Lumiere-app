package com.example.riseandroid.ui.screens.signup.validation

import android.util.Patterns

class ValidateEmail {

    fun execute(email: String): ValidationResult {
        if(email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Email mag niet leeg zijn"
            )
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "De email is niet geldig"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}