package com.example.riseandroid.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.riseandroid.R

data class CustomTheme(
    val logoImg: Int
)

val LocalCustomTheme = staticCompositionLocalOf {
    CustomTheme(logoImg = R.drawable.lumiere_logo)
}
