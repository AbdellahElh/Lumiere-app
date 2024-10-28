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
import com.example.riseandroid.ui.LumiereApp
import com.example.riseandroid.ui.theme.RiseAndroidTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val account = Auth0("it8XxtD6gPwh8XQODS3vrZ4FrtfZoTOG", "alpayozer.eu.auth0.com")
        setContent {
            RiseAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {  innerPadding ->
                    LumiereApp(
                        account = account
                    )
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
            val account = Auth0("it8XxtD6gPwh8XQODS3vrZ4FrtfZoTOG", "alpayozer.eu.auth0.com")
            LumiereApp(
                account = account
            )
        }
    }
}