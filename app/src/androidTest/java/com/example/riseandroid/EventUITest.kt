package com.example.riseandroid

import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.riseandroid.fake.FakeEventRepo
import com.example.riseandroid.fake.FakeProgramRepo
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.ui.screens.homepage.EventItem
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeEventRepo: FakeEventRepo
    private lateinit var fakeProgramRepo: FakeProgramRepo

    @Before
    fun setup() {
        fakeEventRepo = FakeEventRepo()
        fakeProgramRepo = FakeProgramRepo()
    }

    @Test
    fun single_event_displayed_correctly(): Unit = runBlocking {
        composeTestRule.setContent {
            Surface(modifier = Modifier) {
                EventItem(
                    event = EventModel(
                        id = 1,
                        title = "Single Event",
                        genre = "Genre",
                        type = "Type",
                        description = "Description",
                        duration = "90 min",
                        director = "Director",
                        releaseDate = "2024-12-01",
                        videoPlaceholderUrl = null,
                        cover = null,
                        location = "Location",
                        eventLink = null,
                        cinemas = emptyList()
                    ),
                    navController = rememberNavController()
                )
            }
        }

        composeTestRule.onNodeWithText("Single Event").assertExists()
        composeTestRule.onNodeWithText("Description").assertExists()
        composeTestRule.onNodeWithText("Location").assertExists()
    }
}


