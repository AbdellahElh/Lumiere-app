import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.material3.Surface
import com.example.riseandroid.ui.screens.homepage.components.MoviesFilters
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.Modifier
import com.example.riseandroid.data.lumiere.ProgramRepository
import com.example.riseandroid.fake.FakeMovieRepo
import com.example.riseandroid.fake.FakeProgramRepo
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel

class MovieFiltersUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeHomepageViewModel: HomepageViewModel
    private lateinit var fakeMovieRepo: FakeMovieRepo
    private lateinit var fakeProgram:ProgramRepository

    @Before
    fun setup() {
        fakeMovieRepo = FakeMovieRepo()
        fakeProgram = FakeProgramRepo()
        fakeHomepageViewModel = HomepageViewModel(fakeProgram,fakeMovieRepo)
    }

    @Test
    fun filters_are_displayed_on_screen() {
        composeTestRule.setContent {
            Surface(modifier = Modifier) {
                MoviesFilters(homepageViewModel = fakeHomepageViewModel)
            }
        }

        composeTestRule.onNodeWithContentDescription("Selecteer datum").assertIsDisplayed()
        composeTestRule.onNodeWithText("Selecteer Cinema").assertIsDisplayed()
        composeTestRule.onNodeWithText("Toepassen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("MovieTitleInput").assertIsDisplayed()


    }

    @Test
    fun testFiltersSelection() {
        composeTestRule.setContent {
            Surface(modifier = Modifier) {
                MoviesFilters(homepageViewModel = fakeHomepageViewModel)
            }
        }
        composeTestRule.onNodeWithText("Selecteer Cinema").assertIsDisplayed()
        composeTestRule.onNodeWithText("Selecteer Cinema").performClick()

        composeTestRule.onNodeWithText("Brugge").assertExists()
        composeTestRule.onNodeWithText("Antwerpen").assertExists()
        composeTestRule.onNodeWithText("Mechelen").assertExists()
        composeTestRule.onNodeWithText("Cinema Cartoons").assertExists()


        composeTestRule.onNodeWithContentDescription("SelectedAntwerpen")
            .assertDoesNotExist()
        composeTestRule.onNodeWithText("Antwerpen").performClick()
        composeTestRule.onNodeWithContentDescription("SelectedAntwerpen")
            .assertExists()


        composeTestRule.onNodeWithTag("MovieTitleInput").assertTextContains("Film titel...")
        composeTestRule.onNodeWithTag("MovieTitleInput").performClick()
        composeTestRule.onNodeWithTag("MovieTitleInput").performTextInput("Inception")
        composeTestRule.onNodeWithTag("MovieTitleInput").assertTextEquals("Inception")

        composeTestRule.onNodeWithText("Toepassen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Toepassen").performClick()


    }


}
