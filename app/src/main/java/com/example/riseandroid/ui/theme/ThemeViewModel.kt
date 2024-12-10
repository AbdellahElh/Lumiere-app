// ThemeViewModel.kt
package com.example.riseandroid.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeViewModel : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    fun toggleTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }
}
