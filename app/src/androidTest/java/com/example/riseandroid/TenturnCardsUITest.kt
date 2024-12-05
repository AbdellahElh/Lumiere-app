import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.riseandroid.fake.FakeTenturncardRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TenturnCardsUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeRepository: FakeTenturncardRepository
    private lateinit var viewModel: TenturncardViewModel
    lateinit var navController: TestNavHostController


    @Before
    fun setup() {
        fakeRepository = FakeTenturncardRepository()
        viewModel = TenturncardViewModel(tenturncardRepository=fakeRepository)
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    }


    @Test
    fun testCardsAreDisplayed() = runTest {

        composeTestRule.setContent {
            TenturncardScreen(navController,tenTurnCardViewModel = viewModel)
        }
        Thread.sleep(3000)

        composeTestRule.onAllNodesWithText("Tienrittenkaart")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("2024-12-31")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("2024-01-01")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("5")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Bewerken")[0].assertIsDisplayed()

    }

    @Test
    fun addTenturncardFieldIsDisplayed() = runTest {
        composeTestRule.setContent {
            TenturncardScreen(navController,tenTurnCardViewModel = viewModel)
        }
        composeTestRule.onNodeWithTag("codeInputField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("addBtn").assertIsDisplayed()
    }

    @Test
    fun succesMessageIsShown() = runTest {
        composeTestRule.setContent {
            TenturncardScreen( navController, tenTurnCardViewModel = viewModel)
        }
        composeTestRule.onNodeWithTag("codeInputField").performTextInput("succesCode")
        composeTestRule.onNodeWithTag("addBtn").performClick()

        composeTestRule.onNodeWithTag("codeInputField").assertTextContains("Tienbeurtenkaart succesvol toegevoegd")
    }

    @Test
    fun errorMessageIsShown() = runTest {
        composeTestRule.setContent {
            TenturncardScreen(navController,tenTurnCardViewModel = viewModel)
        }
        composeTestRule.onNodeWithTag("codeInputField").performTextInput("errorCode")
        composeTestRule.onNodeWithTag("addBtn").performClick()

        composeTestRule.onNodeWithTag("codeInputField").assertTextContains("Deze kaart behoort al tot iemand")
    }


}