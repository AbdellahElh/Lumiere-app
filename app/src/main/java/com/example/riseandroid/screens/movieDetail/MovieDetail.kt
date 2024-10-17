package com.example.riseandroid.screens.movieDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.riseandroid.R
import com.example.riseandroid.repository.Datasource
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontFamily

@Composable
fun MovieDetailScreen(movieId: Long? , navController: NavController) {
    val movie = Datasource().LoadAllMovies().find { it.movieId == movieId }
    var isExpanded by remember { mutableStateOf(false) }
    if (movie != null) {
        val scrollState = rememberScrollState()

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                modifier = Modifier.verticalScroll(scrollState),

                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.btn_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.popBackStack() }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Details Movie",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = R.drawable.btn_bookmark),
                        contentDescription = "Bookmark",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(movie.posterResourceId),
                        contentDescription = "Movie Poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(400.dp)
                            .clip(RoundedCornerShape(20.dp))
                    )
                }


                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = movie.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Directeur: ${movie.director}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFFBABFC9)
                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Box(
//                        modifier = Modifier
//                            .width(1.dp)
//                            .height(16.dp)
//                            .background(Color(0xFF696D74))
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    Image(
//                        painter = painterResource(id = R.drawable.btn_star),
//                        contentDescription = "Rating",
//                        modifier = Modifier.size(18.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//
//                    Text(
//                        text = "${movie.rating}",
//                        fontSize = 14.sp,
//                        color = Color(0xFFBABFC9)
//                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
                ) {
                    Text(
                        text = movie.genre,
                        fontSize = 16.sp,
                        color = Color(0xFFB2B5BB),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF252932))
                            .padding(8.dp)

                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "${movie.length}",
                        fontSize = 14.sp,
                        color = Color(0xFFB2B5BB),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF252932))
                            .padding(8.dp)
                    )
                }

                Text(
                    text = "Beschrijving",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.padding(top = 28.dp)
                )

                val displayedDescription = if (isExpanded) movie.description else movie.description.take(100)

                Text(
                    text = if (isExpanded) displayedDescription  else "${displayedDescription}...",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(0xFF696D74),
                    modifier = Modifier.padding(top = 16.dp)
                )


                if (movie.description.length > 100) {
                    Text(
                        text = if (isExpanded) "Lees Minder" else "Lees Meer",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFFE5CB77),
                        modifier = Modifier
                            .clickable { isExpanded = !isExpanded }

                    )
                }
                Spacer(modifier = Modifier.height(35.dp))
                NextStepButton(onClick = { /* volgende stap komt hier */ })
                Spacer(modifier = Modifier.height(18.dp))


            }
        }
    } else {
        Text(
            text = "Film niet gevonden",
            color = Color.Red,
            modifier = Modifier.fillMaxSize(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun NextStepButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE5CB77),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(vertical = 15.dp)

    ) {
        Text(
            text = "Volgende Stap",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium


        )
    }
}