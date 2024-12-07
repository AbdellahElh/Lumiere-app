package com.example.riseandroid.ui.screens.scanner

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.riseandroid.MainActivity

@Composable
fun ScanCodeScreen(
    navController: NavHostController? = rememberNavController(),
    viewModel: ScannerViewModel = viewModel(factory = ScannerViewModel.Factory),
) {
    val scannerState = viewModel.scannerState

    Column {
        Button(
            modifier = Modifier.testTag("startScannerBtn"),
            onClick = { viewModel.launchScanner() }
        ) {
            Text("Start met scannen")
        }
        Button(
            modifier = Modifier.testTag("navigateBackBtn"),
            onClick = { navController?.popBackStack() }
        ) {
            Text("Ga terug")
        }
    }
}




