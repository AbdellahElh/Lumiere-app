package com.example.riseandroid.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.viewmodel.TenturncardViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.riseandroid.ui.theme.RiseAndroidTheme


class TenturncardScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RiseAndroidTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val viewModel = ViewModelProvider(this).get(TenturncardViewModel::class.java)
                    val tenturncards by remember { mutableStateOf(viewModel.tenturncards) }


                    LaunchedEffect(Unit) {
                        viewModel.fetchTenturncards()
                    }


                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(tenturncards) { card ->
                            TenturncardCard(tenturncard = card)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun TenturncardCard(tenturncard: Tenturncard) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
           Text(
                text = "Tienrittenkaart",

            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Vervaldatum: ${tenturncard.expirationDate}")
            Text("Aankoopdatum: ${tenturncard.purchaseDate}")
            Text("Beurten over: ${tenturncard.amountLeft} / 10")
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { /* Edit card */ }) {
                    Text("Bewerken")
                }
                Button(onClick = { /* More info */ }) {
                    Text("Meer info")
                }
            }
        }
    }

}
