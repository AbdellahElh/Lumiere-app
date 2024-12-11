import androidx.compose.material3.Scaffold
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.auth0.android.Auth0
import com.example.riseandroid.fake.FakeAuthViewModel
import com.example.riseandroid.fake.FakeForgotPasswordViewModel
import com.example.riseandroid.navigation.BottomBar
import com.example.riseandroid.navigation.BottomBarScreen
import com.example.riseandroid.navigation.NavHostWrapper
import com.example.riseandroid.ui.theme.ThemeViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    // Use the fake implementations
    private val fakeAuthViewModel = FakeAuthViewModel()
    private val fakeForgotPasswordViewModel = FakeForgotPasswordViewModel()

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
                    navController = navController,
                    paddingValues = paddingValues,
                    account = mockAuth0,
                    authViewModel = fakeAuthViewModel,
                    forgotPasswordViewModel = fakeForgotPasswordViewModel,
                    themeViewModel = ThemeViewModel()
                )
            }
        }
    }

    @Test
    fun rallyNavHost_verifyOverviewStartDestination() {
        // Verify that the correct start destination is displayed
        composeTestRule
            .onNodeWithContentDescription("Navigation Icon for Home", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun bottomBar_clickAccount_navigatesToAccount() {
        // Perform click on the Account navigation icon
        composeTestRule.onNodeWithContentDescription(
            "Navigation Icon for Account",
            useUnmergedTree = true
        ).performClick()

        // Assert that the current route is Account
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        assertEquals(BottomBarScreen.Account.route, currentRoute)
    }

    @Test
    fun bottomBar_clickWatchlist_navigatesToWatchlist() {
        // Perform click on the Watchlist navigation icon
        composeTestRule.onNodeWithContentDescription(
            "Navigation Icon for Watchlist",
            useUnmergedTree = true
        ).performClick()

        // Assert that the current route is Watchlist
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        assertEquals(BottomBarScreen.Watchlist.route, currentRoute)
    }

    @Test
    fun bottomBar_clickTickets_navigatesToTickets() {
        // Perform click on the Tickets navigation icon
        composeTestRule.onNodeWithContentDescription(
            "Navigation Icon for Tickets",
            useUnmergedTree = true
        ).performClick()

        // Assert that the current route is Tickets
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        assertEquals(BottomBarScreen.Tickets.route, currentRoute)
    }
}
