
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText

import androidx.compose.material3.Surface
import androidx.navigation.testing.TestNavHostController

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.core.app.ApplicationProvider
import com.example.riseandroid.fake.FakeAuthRepo
import com.example.riseandroid.ui.screens.account.AuthViewModel
import com.example.riseandroid.ui.screens.signup.SignUp
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignUpUITest {

    @get:Rule
    val signUpTestRule = createComposeRule()
    lateinit var navController: TestNavHostController
    lateinit var authViewModel: AuthViewModel

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        authViewModel = AuthViewModel(
            authRepo = FakeAuthRepo()
        )
    }

    @Test
    fun signUpScreen_Displays_Correct_Elements() {
        signUpTestRule.setContent {
            Surface(modifier = Modifier) {
                SignUp(
                    signUp = {},
                    navController = navController,
                    authViewModel = authViewModel,

                )
            }
        }
        signUpTestRule.onNodeWithContentDescription("Register Screen").assertIsDisplayed()
        signUpTestRule.onNodeWithText("Email").assertIsDisplayed()
        signUpTestRule.onNodeWithText("Wachtwoord").assertIsDisplayed()
        signUpTestRule.onNodeWithContentDescription("Register Button").assertIsDisplayed()
    }

//    @Test
//    fun signUp_Success() {
//        var receivedCredentials: Credentials? = null
//        signUpTestRule.setContent {
//            Surface(modifier = Modifier) {
//                SignUp(
//                    signUp = {},
//                    navController = navController,
//                    authViewModel = authViewModel
//                )
//            }
//        }
//
//        signUpTestRule.onNodeWithText("Email").performTextInput("test@example.com")
//        signUpTestRule.onNodeWithText("Wachtwoord").performTextInput("Password123*")
//        signUpTestRule.onNodeWithContentDescription("Register Button").performClick()
//
//        assertNotNull(receivedCredentials)
//        assertEquals("fakeIdToken", receivedCredentials?.idToken)
//        assertEquals("fakeAccessToken", receivedCredentials?.accessToken)
//    }

}
