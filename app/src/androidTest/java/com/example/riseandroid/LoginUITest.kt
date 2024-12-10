
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.auth0.android.result.Credentials
import com.example.riseandroid.fake.FakeAuthRepo
import com.example.riseandroid.fake.FakeMovieRepo
import com.example.riseandroid.fake.FakeUserManager
import com.example.riseandroid.fake.FakeWatchlistRepo
import com.example.riseandroid.ui.screens.account.AuthViewModel
import com.example.riseandroid.ui.screens.login.LoginScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginUITest {

    @get:Rule
    val loginTestRule = createComposeRule()

    lateinit var navController: TestNavHostController
    lateinit var authViewModel: AuthViewModel

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        authViewModel = AuthViewModel(
            authRepo = FakeAuthRepo(),
            watchlistRepo = FakeWatchlistRepo(),
            userManager = FakeUserManager(),
            application = ApplicationProvider.getApplicationContext(),
            movieRepo = FakeMovieRepo()
        )
    }

    @Test
    fun loginScreen_Displays_Correct_Elements() {
        loginTestRule.setContent {
            Surface {
                val fakeLogin: (Credentials) -> Unit = {}
                LoginScreen(
                    navController = navController,
                    login = fakeLogin,
                    authViewModel = authViewModel
                )
            }
        }

        val loginNodes = loginTestRule.onAllNodesWithText("Login")

        loginNodes[0].assertIsDisplayed()
        loginNodes[1].assertIsDisplayed()

        loginTestRule.onNodeWithText("Email").assertIsDisplayed()
        loginTestRule.onNodeWithText("Password").assertIsDisplayed()
    }

}
