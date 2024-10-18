package com.example.riseandroid

import com.example.riseandroid.fake.FakeDataSource
import com.example.riseandroid.fake.FakeNetworkMoviesRepository
import com.example.riseandroid.rules.TestDispatcherRule
import com.example.riseandroid.ui.screens.homepage.HomepageUiState
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class HomepageViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun homepageViewModel_getMovies_verifyHomepageUiStateSucces() {

        val homepageViewModel = HomepageViewModel(
            moviesRepository = FakeNetworkMoviesRepository()
        )

        val recentMovies = FakeDataSource.LoadRecentMoviesMock()

        val nonRecentMovies = FakeDataSource.LoadNonRecenMoviesMock()

        val actualState = homepageViewModel.homepageUiState

        if (actualState is HomepageUiState.Succes) {
            Assert.assertEquals(recentMovies, actualState.recentMovies.run { recentMovies })
            Assert.assertEquals(nonRecentMovies, actualState.run { nonRecentMovies })
        } else {
            Assert.fail("Expected HomepageUiState.Succes but got $actualState")
        }
    }
}