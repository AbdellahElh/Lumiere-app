package com.example.riseandroid.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Moon
import com.composables.icons.lucide.Sun

@Composable
fun ThemeToggle(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onThemeChange(!isDarkTheme) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Lucide.Sun,
            contentDescription = "Light Mode",
            modifier = Modifier.size(24.dp)
        )

        Switch(
            checked = isDarkTheme,
            onCheckedChange = { onThemeChange(it) }
        )

        Icon(
            imageVector = Lucide.Moon,
            contentDescription = "Dark Mode",
            modifier = Modifier.size(24.dp)
        )
    }
}