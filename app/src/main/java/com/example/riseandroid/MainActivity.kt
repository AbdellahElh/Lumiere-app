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

class MainActivity : ComponentActivity() {
    private lateinit var appContainer: AppContainer

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        appContainer = (application as LumiereApplication).container
        setContent {
            RiseAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {  innerPadding ->
                    LumiereApp(account = appContainer.authRepo.auth0)
                }
            }
        }
    }
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun AppPreview() {
    RiseAndroidTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LumiereApp(account = Auth0("it8XxtD6gPwh8XQODS3vrZ4FrtfZoTOG", "alpayozer.eu.auth0.com"))
        }
    }
}