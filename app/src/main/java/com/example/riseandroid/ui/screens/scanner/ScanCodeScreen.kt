package com.example.riseandroid.ui.screens.scanner

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ScanCodeScreen(
    navController: NavHostController? = rememberNavController(),
    viewModel: ScannerViewModel = viewModel(factory = ScannerViewModel.Factory),
) {
    val scannerState = viewModel.scannerState
    val actionState = viewModel.actionState

    Box() {
        viewModel.launchScanner()
        // Btn to scan
        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = { viewModel.onScanResult("hier komen de resultaten van de scan") }
        ) {
            Text("Scan")
        }
        // Btn to navigate back
        Button(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = { navController?.popBackStack() }
        ) {
            Text("Ga terug")
        }
        // TODO btn to switch camera
    }
}




