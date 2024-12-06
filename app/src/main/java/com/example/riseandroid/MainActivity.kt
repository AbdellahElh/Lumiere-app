package com.example.riseandroid

import android.Manifest
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
//import android.content.Context.getSystemService
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias LumaListener = (luma: Double) -> Unit

class MainActivity : ComponentActivity() {
    private lateinit var appContainer: AppContainer
    private lateinit var cameraExecutor: ExecutorService

    // ActivityResultLauncher for requesting permissions
    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle permission results
        if (permissions.all { it.value }) {
            setupCamera()
        } else {
            // Handle case where permissions are denied

        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeApp()

        if (allPermissionsGranted()) {
            setupCamera()
        } else {
            // Request permissions
            permissionsLauncher.launch(REQUIRED_PERMISSIONS)
        }

        setContent {
            MainContent()
        }
    }

    private fun initializeApp() {
        createNotificationChannel()
        enableEdgeToEdge()
        appContainer = (application as LumiereApplication).container
        cameraExecutor = Executors.newSingleThreadExecutor()
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

    private fun setupCamera() {
        val previewView: PreviewView = ActivityMainBinding.viewFinder // Ensure viewFinder is set up in your layout
        val cameraController = LifecycleCameraController(this).apply {
            bindToLifecycle(this@MainActivity)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
        previewView.controller = cameraController
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = listOf(
            Manifest.permission.CAMERA
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
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
