package com.example.riseandroid.ui

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import com.example.riseandroid.ui.screens.homepage.Homepage

@Composable
fun LumiereApp() {
    Scaffold () {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Homepage(
                contentPadding = it
                )
        }
    }
}