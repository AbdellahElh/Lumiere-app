package com.example.riseandroid

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.riseandroid.fake.FakeTenturncardRepository
import com.example.riseandroid.ui.screens.scanner.ScanCodeScreen
import com.example.riseandroid.ui.screens.scanner.ScannerAction
import com.example.riseandroid.ui.screens.scanner.ScannerViewModel
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ScannerUITest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeRepository: FakeTenturncardRepository
    private lateinit var viewModel: ScannerViewModel
    lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        fakeRepository = FakeTenturncardRepository()
        viewModel = ScannerViewModel(tenturncardRepository=fakeRepository)
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        composeTestRule.setContent {
            ScanCodeScreen(navController, viewModel)
        }
    }

    @Test
    fun allBtnsAreDisplayed() = runTest {
        composeTestRule.onNodeWithTag("startScannerBtn").isDisplayed()
        composeTestRule.onNodeWithTag("navigateBackBtn").isDisplayed()
    }

    @Test
    fun cameraIsDisplayed() = runTest {
        composeTestRule.onNodeWithTag("startScannerBtn").performClick()

        //Since we can't directly test if the camera is being used, we test the state
        assertTrue(viewModel.scannerState.equals(ScannerAction.LaunchScanner))
    }
}