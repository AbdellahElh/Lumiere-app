import androidx.compose.material3.Scaffold
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.auth0.android.Auth0
import com.example.riseandroid.navigation.BottomBar
import com.example.riseandroid.navigation.BottomBarScreen
import com.example.riseandroid.navigation.NavHostWrapper
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            // Initialize the TestNavHostController
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            val mockAuth0 = Auth0("clientId", "domain")

            // Set up the BottomBar and NavHostWrapper for testing
            Scaffold(
                bottomBar = { BottomBar(navController = navController) }
            ) { paddingValues ->
                NavHostWrapper(
                    navController = navController, paddingValues = paddingValues,
                    account = mockAuth0,
                    authViewModel = TODO(),
                    forgotPasswordViewModel = TODO()
                )
            }
        }
    }

    @Test
    fun rallyNavHost_verifyOverviewStartDestination() {
        composeTestRule
            .onNodeWithContentDescription("Home Screen")
            .assertIsDisplayed()
    }
    @Test
    fun bottomBar_clickAccount_navigatesToAccount() {

        composeTestRule.onNodeWithContentDescription("Navigation Icon for Account",
            useUnmergedTree = true)
            .performClick()

        val currentRoute = navController.currentBackStackEntry?.destination?.route
        assertEquals(BottomBarScreen.Account.route, currentRoute)
    }
    @Test
    fun bottomBar_clickScan_navigatesToScan() {

        composeTestRule.onNodeWithContentDescription("Navigation Icon for Scan",
            useUnmergedTree = true)
            .performClick()

        val currentRoute = navController.currentBackStackEntry?.destination?.route
        assertEquals(BottomBarScreen.ScanCode.route, currentRoute)
    }
    @Test
    fun bottomBar_clickTickets_navigatesToTickets() {

        composeTestRule.onNodeWithContentDescription("Navigation Icon for Tickets",
            useUnmergedTree = true)
            .performClick()

        val currentRoute = navController.currentBackStackEntry?.destination?.route
        assertEquals(BottomBarScreen.Tickets.route, currentRoute)
    }

}
