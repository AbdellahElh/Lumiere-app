package com.example.riseandroid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.auth0.android.Auth0
import com.example.riseandroid.data.AppContainer
import com.example.riseandroid.ui.LumiereApp
import com.example.riseandroid.ui.theme.RiseAndroidTheme
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.riseandroid.ui.screens.scanner.ScannerAction
import com.example.riseandroid.ui.screens.scanner.ScannerState
import com.example.riseandroid.ui.screens.scanner.ScannerViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var appContainer: AppContainer
    private val scannerViewModel: ScannerViewModel by viewModels()

    private val scanLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
        scannerViewModel.onScanResult(result.contents)
    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        scannerViewModel.checkCameraPermission(isGranted)
    }


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Observe state changes from the Scanner ViewModel
        lifecycleScope.launch {
            scannerViewModel.scannerState.collect { state ->
                handleScannerState(state)
            }
        }

        // Observe actions from the Scanner ViewModel
        lifecycleScope.launch {
            scannerViewModel.actionState.collect { action ->
                handleScannerAction(action)
            }
        }
        initializeApp()
        setContent {
            MainContent()
        }
    }

    private fun initializeApp() {
        createNotificationChannel(this)
        enableEdgeToEdge()
        appContainer = (application as LumiereApplication).container
    }


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    private fun MainContent() {
        RiseAndroidTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) {
                LumiereApp(account = appContainer.authRepo.auth0)
            }
        }
    }

    private fun handleScannerState(state: ScannerState) {
        when (state) {
            is ScannerState.ShowResult -> {
                Toast.makeText(this, "Result: ${state.result}", Toast.LENGTH_SHORT).show()
            }
            ScannerState.Cancelled -> {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show()
            }
            is ScannerState.ShowToast -> {
                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
            is ScannerState.Loading -> null
        }
    }

    private fun handleScannerAction(action: ScannerAction?) {
        when (action) {
            ScannerAction.LaunchScanner -> null
            ScannerAction.RequestCameraPermission -> requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            null -> Unit // No action to perform
        }
    }

    private fun launchScanner() {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Scan QR code")
            setCameraId(0)
            setBeepEnabled(false)
            setBarcodeImageEnabled(true)
            setOrientationLocked(false)
        }
        scanLauncher.launch(options)
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun AppPreview() {
    RiseAndroidTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LumiereApp(account = Auth0("UVn7L1s6FcWogUb9Y8gLm9HoJQzS5xK9", "dev-viwl48rh7lran3ul.us.auth0.com"))
        }
    }
}


private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "ReminderChannel"
        val descriptionText = "Channel for Movie Reminders"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("REMINDER_CHANNEL", name, importance).apply {
            description = descriptionText
        }

        // Get the NotificationManager from the provided context
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
