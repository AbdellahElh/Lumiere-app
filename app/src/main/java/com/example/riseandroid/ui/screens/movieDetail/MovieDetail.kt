package com.example.riseandroid.ui.screens.movieDetail

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.riseandroid.R
import com.example.riseandroid.model.Movie
import com.example.riseandroid.ui.screens.homepage.ErrorScreen
import com.example.riseandroid.ui.screens.homepage.LoadingScreen
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailViewModel
import com.example.riseandroid.ui.screens.movieDetail.MovieDetailUiState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextField
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import java.util.Calendar


@Composable
fun MovieDetailScreen(
    movieId: Long,
    navController: NavController,
    viewModel: MovieDetailViewModel = viewModel(factory = MovieDetailViewModel.provideFactory(movieId))
) {
    when (val uiState = viewModel.movieDetailUiState) {
        is MovieDetailUiState.Loading -> {
            LoadingScreen()
        }
        is MovieDetailUiState.Error -> {
            ErrorScreen()
        }
        is MovieDetailUiState.Success -> {
            val movie = uiState.specificMovie
            MovieDetailContent(movie = movie, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailContent(movie: Movie, navController: NavController) {
    var isExpanded by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
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
            MovieDetailHeader(navController)
            Spacer(modifier = Modifier.height(32.dp))
            MoviePoster(movie)
            Spacer(modifier = Modifier.height(20.dp))
            MovieInfo(movie)
            Spacer(modifier = Modifier.height(10.dp))
            MovieDescription(movie, isExpanded) { isExpanded = !isExpanded }
            Spacer(modifier = Modifier.height(35.dp))
            NextStepButton(onClick = { showBottomSheet = true })
            Spacer(modifier = Modifier.height(18.dp))
        }

        if (showBottomSheet) {
            val context = LocalContext.current
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = rememberModalBottomSheetState()
            ) {
                BottomSheetContent(context) { showBottomSheet = false }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(context: Context, onDismiss: () -> Unit) {
    val cinema = arrayOf("Brugge", "Antwerpen", "Gent", "dsfdsf")
    var selectedText by remember { mutableStateOf(cinema[0]) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val date = remember { mutableStateOf(getCurrentDate()) }
    val selectedHour = remember { mutableStateOf("12:00") }
    var isTimeDropdownExpanded by remember { mutableStateOf(false) }
    val hours = arrayOf("10:00", "11:00", "12:00", "13:00", "14:00")

    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth -> date.value = "$dayOfMonth/${month + 1}/$year" },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE5CB77)) // Set the background color here
            .padding(16.dp) // Add some padding
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Select Cinema",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Cinema Dropdown
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .clickable { isDropdownExpanded = true }, // Open dropdown on click
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(color = Color.Black)
                )

                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    cinema.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedText = item
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Date and Time Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Date Picker
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Datum",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1C1F26),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Box(
                        modifier = Modifier
                            .border(
                                1.dp,
                                Color(0xFF1C1F26).copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { datePickerDialog.show() }
                    ) {
                        TextField(
                            value = date.value,
                            onValueChange = {},
                            readOnly = true,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = TextStyle(color = Color.Black)
                        )
                    }
                }

                // Time Picker
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tijd",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1C1F26),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = isTimeDropdownExpanded,
                        onExpandedChange = { isTimeDropdownExpanded = !isTimeDropdownExpanded }
                    ) {
                        TextField(
                            value = selectedHour.value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTimeDropdownExpanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isTimeDropdownExpanded = true },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = TextStyle(color = Color.Black)
                        )

                        ExposedDropdownMenu(
                            expanded = isTimeDropdownExpanded,
                            onDismissRequest = { isTimeDropdownExpanded = false }
                        ) {
                            hours.forEach { hour ->
                                DropdownMenuItem(
                                    text = { Text(text = hour) },
                                    onClick = {
                                        selectedHour.value = hour
                                        isTimeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Checkout button
            Button(
                onClick = {
                    Toast.makeText(
                        context,
                        "Selected Cinema: $selectedText",
                        Toast.LENGTH_SHORT
                    ).show()
                    onDismiss() // Close bottom sheet
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Checkout")
            }
        }
    }
}
fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    return "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
}
    @Composable
    fun MovieDetailHeader(navController: NavController) {
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
    }

    @Composable
    fun MoviePoster(movie: Movie) {
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
    }

    @Composable
    fun MovieInfo(movie: Movie) {
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
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
        ) {
            Text(
                text = "${movie.genre}",
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
    }

    @Composable
    fun MovieDescription(movie: Movie, isExpanded: Boolean, onToggleExpand: () -> Unit) {
        Text(
            text = "Beschrijving",
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.padding(top = 28.dp)
        )

        val displayedDescription =
            if (isExpanded) movie.description else movie.description.take(100)

        Text(
            text = if (isExpanded) displayedDescription else "$displayedDescription...",
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
                    .clickable { onToggleExpand() }
            )
        }
    }

    @Composable
    fun NextStepButton(onClick: () -> Unit) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
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


//@Composable
//fun CinemaSelectionSheet(
//    onDismiss: () -> Unit,
//    onCheckout: (String, Int, String, String) -> Unit
//) {
//    var selectedCinema by remember { mutableStateOf("") }
//    var numberOfPeople by remember { mutableStateOf(1) }
//    var selectedDate by remember { mutableStateOf("") }
//    var selectedTime by remember { mutableStateOf("") }
//
//    // State to control dropdown visibility
//    var isDropdownExpanded by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier.padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Text(text = "Select Cinema", fontSize = 20.sp, fontWeight = FontWeight.Bold)
//
//        // Cinema selection
//        Box {
//            TextField(
//                value = selectedCinema,
//                onValueChange = { },
//                label = { Text("Select Cinema") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { isDropdownExpanded = true },
//                readOnly = true // Prevent text entry; only dropdown selection allowed
//            )
//
//            DropdownMenu(
//                expanded = isDropdownExpanded,
//                onDismissRequest = { isDropdownExpanded = false }
//            ) {
//                val cinemas = listOf("Brugge", "Antwerpen", "Serf")
//                cinemas.forEach { cinema ->
//                    DropdownMenuItem(onClick = {
//                        selectedCinema = cinema
//                        isDropdownExpanded = false
//                    }) {
//                        Text(text = cinema)
//                    }
//                }
//            }
//        }
//
//        // Input for number of people
//        TextField(
//            value = numberOfPeople.toString(),
//            onValueChange = { numberOfPeople = it.toIntOrNull() ?: 1 },
//            label = { Text("Number of People") },
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
//        )
//
//        // Date picker
//        TextField(
//            value = selectedDate,
//            onValueChange = { selectedDate = it },
//            label = { Text("Date") },
//            modifier = Modifier.fillMaxWidth(),
//            // Implement a date picker dialog if needed
//        )
//
//        // Time picker
//        TextField(
//            value = selectedTime,
//            onValueChange = { selectedTime = it },
//            label = { Text("Time") },
//            modifier = Modifier.fillMaxWidth(),
//            // Implement a time picker dialog if needed
//        )
//
//        // Checkout button
//        Button(
//            onClick = {
//                onCheckout(selectedCinema, numberOfPeople, selectedDate, selectedTime)
//                onDismiss()
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = "Checkout")
//        }
//    }
//}
