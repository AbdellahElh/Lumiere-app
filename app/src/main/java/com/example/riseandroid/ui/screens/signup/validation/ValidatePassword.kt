package com.example.riseandroid.ui.screens.signup.validation

class ValidatePassword {
    fun execute(password: String): ValidationResult {
        val regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$".toRegex()

        if (!regex.matches(password)) {
            return ValidationResult(
                successful = false,
                errorMessage = "Wachtwoord moet 8+ tekens, 1 cijfer, 1 hoofdletter en 1 speciaal teken bevatten"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}
