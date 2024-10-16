package com.example.riseandroid

import com.example.riseandroid.fake.FakeDataSource
import com.example.riseandroid.fake.FakeNetworkMoviesRepository
import com.example.riseandroid.rules.TestDispatcherRule
import com.example.riseandroid.ui.screens.homepage.HomepageUiState
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import org.junit.Assert
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class HomepageViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun homepageViewModel_getRecentMovies_verifyHomepageUiStateSucces() {

        val homepageViewModel = HomepageViewModel(
            moviesRepository = FakeNetworkMoviesRepository()
        )
        Assert.assertEquals(
            HomepageUiState.Succes(
                "\"Success: ${FakeDataSource.LoadRecentMoviesMock().size} movies retrieved"
            ),
            homepageViewModel.homepageUiState
        )
    }
}