package com.example.riseandroid

import android.app.Application
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.riseandroid.ui.theme.RiseAndroidTheme

class App {

    @Composable
    fun lumiereApp() {
        // Move the content setup from MainActivity to here

            RiseAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }

    }
}


@Composable
fun GreetingImage(modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.screenshot_2024_10_08_105150)
        Box(modifier.fillMaxSize()) {
            Row(modifier) {
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    alpha = 0.5F
                )
                Image(
                    painter = image,
                    contentDescription = null,
                    alpha = 0.5F
                )
            }
            GreetingText(
                message = stringResource(R.string.nieuwe_films),
                modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
            )
            GreetingText(
                message = stringResource(R.string.alle_films),
                modifier = Modifier.padding(0.dp)
            )
        }
    }

@Composable
fun GreetingText(message: String, modifier: Modifier = Modifier) {
        androidx.compose.material3.Text(
            text = message,
            fontSize = 100.sp,
            modifier = modifier.padding(8.dp)
        )
    }



