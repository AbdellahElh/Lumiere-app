package com.example.riseandroid.ui

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.riseandroid.ui.screens.homepage.Homepage
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel

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