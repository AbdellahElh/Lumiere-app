package com.example.riseandroid.ui.screens.homepage.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.riseandroid.model.MovieModel

@Composable
fun MoviePoster(movie: MovieModel, navController: NavHostController, modifier: Modifier = Modifier) {
    val imageUrl = movie.cover

    Column(modifier = modifier.clickable {
        navController.navigate("movieDetail/${movie.id}")
    }.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .shadow(
                    elevation = 6.dp,
                    spotColor = Color.White,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        )  {
            AsyncImage(
                model = imageUrl,
                contentDescription = movie.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )
        }
        Text(
            text = movie.name,
            modifier = Modifier.fillMaxWidth().padding(8.dp).testTag(movie.name),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
