import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.riseandroid.fake.FakeTenturncardRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TenturnCardsUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeRepository: FakeTenturncardRepository
    private lateinit var viewModel: TenturncardViewModel

    @Before
    fun setup() {
        fakeRepository = FakeTenturncardRepository()
        viewModel = TenturncardViewModel(tenturncardRepository=fakeRepository)
        composeTestRule.setContent {
            TenturncardScreen(tenTurnCardViewModel = viewModel)
        }
    }


    @Test
    fun testCardsAreDisplayed() = runTest {
        Thread.sleep(3000)

        composeTestRule.onAllNodesWithText("Tienrittenkaart")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("2024-12-31")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("2024-01-01")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("5")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Bewerken")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("qrCode")[0].assertIsDisplayed()

    }
    @Test
    fun valueIsChangedWhenQrCodeIsClicked() = runTest {
        Thread.sleep(3000)
        composeTestRule.onAllNodesWithTag("qrCode")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("qrCode")[0].performClick()
        composeTestRule.onAllNodesWithText("5")[0].assertIsDisplayed()
    }

    @Test
    fun addTenturncardFieldIsDisplayed() = runTest {
        composeTestRule.onNodeWithTag("codeInputField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("addBtn").assertIsDisplayed()
    }

    @Test
    fun succesMessageIsShown() = runTest {
        composeTestRule.onNodeWithTag("codeInputField").performTextInput("succesCode")
        composeTestRule.onNodeWithTag("addBtn").performClick()

        composeTestRule.onNodeWithTag("codeInputField").assertTextContains("Tienbeurtenkaart succesvol toegevoegd")
    }

    @Test
    fun errorMessageIsShown() = runTest {
        composeTestRule.onNodeWithTag("codeInputField").performTextInput("errorCode")
        composeTestRule.onNodeWithTag("addBtn").performClick()

        composeTestRule.onNodeWithTag("codeInputField").assertTextContains("Deze kaart behoort al tot iemand")
    }

    @Test
    fun editFieldIsShownWhenBtnIsClicked() = runTest {
        Thread.sleep(3000)
        composeTestRule.onAllNodesWithText("Tienrittenkaart")[0].assertIsDisplayed()


        composeTestRule.onAllNodesWithTag("editCardBtn")[0].performClick()
        composeTestRule.onNodeWithTag("EditTextField").isDisplayed()
    }
}