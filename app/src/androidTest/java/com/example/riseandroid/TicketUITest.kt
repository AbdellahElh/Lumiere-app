
package com.example.riseandroid

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.riseandroid.fake.FakeTicketRepository
import com.example.riseandroid.fake.FakeTicketViewModel
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.ui.screens.ticket.TicketScreen
import com.example.riseandroid.ui.screens.ticket.TicketUiState
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
    fun testFakeTicketViewModel() {
        val fakeRepo = FakeTicketRepository()
        val fakeViewModel = FakeTicketViewModel(fakeRepo)

        composeTestRule.setContent {
           val navController = rememberNavController()
            TicketScreen(
                viewModel = fakeViewModel, authToken = "fakeAuthToken",
                userId = 1,
                navController = navController
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            fakeViewModel.ticketUiState is TicketUiState.Success
        }

        composeTestRule.onNodeWithText("1").assertIsDisplayed()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
    }
    @Test
    fun ticketScreen_DisplaysNoTicketsMessage_WhenNoTicketsAvailable() {
        val emptyRepo = object : FakeTicketRepository() {
            override suspend fun getTickets(): Flow<List<Ticket>> = flowOf(emptyList())
        }
        val viewModel = FakeTicketViewModel(ticketRepository = emptyRepo)

        composeTestRule.setContent {
            val navController = rememberNavController()
            TicketScreen(
                viewModel = viewModel,
                authToken = "fakeAuthToken",
                userId = 1,
                navController = navController
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            viewModel.ticketUiState is TicketUiState.Success
        }

        composeTestRule.onNodeWithText("Geen Tickets").assertIsDisplayed()
    }

}
