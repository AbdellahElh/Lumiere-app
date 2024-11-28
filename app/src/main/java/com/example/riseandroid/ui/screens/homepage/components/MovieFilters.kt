package com.example.riseandroid.ui.screens.homepage.components
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import com.example.riseandroid.util.getTodayDate
import java.util.Calendar
import java.util.Locale

@Composable
fun MoviesFilters(
    homepageViewModel:HomepageViewModel
) {

    val selectedDate by homepageViewModel.selectedDate.collectAsState()
    val selectedCinemas by homepageViewModel.selectedCinemas.collectAsState()
    val searchTitle by homepageViewModel.searchTitle.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DatePicker(
                selectedDate = selectedDate,
                onDateSelected = { selectedLocalDate ->
                    homepageViewModel.updateFilters(
                        selectedLocalDate,
                        selectedCinemas,
                        searchTitle ?: "",
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CinemaDropDown(
                selectedCinemas = selectedCinemas,
                onCinemaSelected = { updatedCinemas ->
                    homepageViewModel.updateFilters(
                        selectedDate,
                        updatedCinemas,
                        searchTitle ?: "",
                    )
                })

            TitleSearch(
                title = searchTitle ?: "",
                onTitleChanged = { updatedTitle ->
                    homepageViewModel.updateFilters(selectedDate, selectedCinemas, updatedTitle)
                }
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        homepageViewModel.updateFilters(getTodayDate(),
                            emptyList(),
                            "")
                        homepageViewModel.applyFilters()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF72d4d4),
                        contentColor = Color.White
                    ),
                ) {
                    Text(text = "Reset")
                }

                Button(
                    onClick = {
                        homepageViewModel.applyFilters()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE5CB77),
                        contentColor = Color.White
                    ),
                ) {
                    Text(text = "Toepassen")
                }
            }
        }
    }
}


@Composable
fun DatePicker(selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current

    var dateText by remember {
        mutableStateOf(
            selectedDate.split("-").let { (year, month, day) ->
                "$day-$month-$year"
            }
        )
    }

    Button(onClick = {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, day)

                val formattedDateForApi = String.format(
                    Locale.getDefault(),
                    "%04d-%02d-%02d",
                    year, month + 1, day
                )
                dateText = String.format(
                    Locale.getDefault(),
                    "%02d-%02d-%04d",
                    day, month, year
                );
                onDateSelected(formattedDateForApi)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    },
        modifier = Modifier.wrapContentWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE5CB77),
            contentColor = Color.White
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = dateText,
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Selecteer datum",
                modifier = Modifier.padding(start = 8.dp),

                )
        }
    }

}


@Composable
fun CinemaDropDown(selectedCinemas: List<String>,
                   onCinemaSelected: (List<String>) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    val cinemaOptions = listOf("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")

    fun toggleCinemaSelection(cinema: String) {
        val updatedCinemas = if (cinema in selectedCinemas) {
            selectedCinemas - cinema
        } else {
            selectedCinemas + cinema
        }
        onCinemaSelected(updatedCinemas)
    }


    Column {
        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier.wrapContentWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE5CB77),
                contentColor = Color.White
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(text = "Selecteer Cinema")
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 30.dp)
        ) {
            cinemaOptions.forEach { cinema ->
                DropdownMenuItem(
                    text = { Text(cinema,modifier = Modifier.testTag(cinema)) },
                    onClick = { toggleCinemaSelection(cinema) },
                    trailingIcon = {
                        if (cinema in selectedCinemas) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected${cinema}"
                            )
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun TitleSearch(title: String, onTitleChanged: (String) -> Unit) {
    var text by remember { mutableStateOf(title) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextField(
            value = text,
            onValueChange = { newValue ->
                text = newValue
                onTitleChanged(newValue)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .shadow(1.dp, shape = MaterialTheme.shapes.small)
                .testTag("MovieTitleInput")
            ,
            singleLine = true,
            placeholder = {
                Text(
                    text = "Film titel...",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE5CB77),
                unfocusedContainerColor = Color(0xFFE5CB77),
                cursorColor = Color.Black
            )
        )
    }
}




@Preview(showBackground = true)
@Composable
fun MoviesFiltersPreview() {
    val viewModel: HomepageViewModel = viewModel(
        factory = HomepageViewModel.Factory
    )

    MoviesFilters(homepageViewModel = viewModel)
}