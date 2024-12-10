
package com.example.riseandroid

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.riseandroid.fake.FakeTicketRepository
import com.example.riseandroid.fake.FakeTicketViewModel
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.ui.screens.ticket.TicketScreen
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TicketUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val fakeRepo = FakeTicketRepository()
    @Test
    fun ticketScreen_DisplaysTickets_WhenTicketsAreAvailable() {
        val viewModel = FakeTicketViewModel(
            ticketRepository = fakeRepo
        )

        composeTestRule.setContent {
            val navController = rememberNavController()
            TicketScreen(
                userId = 1L,
                authToken = "dummyAuthToken",
                navController = navController,
                viewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("1").assertIsDisplayed()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
    }

}
