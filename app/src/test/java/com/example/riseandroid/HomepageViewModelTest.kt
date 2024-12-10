// HomepageViewModelTest.kt
package com.example.riseandroid

import com.example.riseandroid.fake.FakeEventRepo
import com.example.riseandroid.fake.FakeMoviePosterRepo
import com.example.riseandroid.fake.FakeMovieRepo
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.network.ResponseMovie
import com.example.riseandroid.rules.MainDispatcherRule
import com.example.riseandroid.ui.screens.homepage.HomepageUiState
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomepageViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun homepageViewModel_getMoviesAndEvents_verifyHomepageUiStateSuccess() = runTest {
        val fakeMoviePosters = listOf(
            MoviePoster(
                id = 1,
                cover = "https://example.com/poster1.jpg",
                releaseDate = "2023-10-01T00:00:00"
            )
        )

        val fakeMovies = listOf(
            ResponseMovie(
                id = 1,
                title = "Fake Movie1",
                cinemas = emptyList(),
                cast = emptyList(),
                coverImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
                genre = "",
                duration = 100,
                director = "",
                description = "",
                videoPlaceholderUrl = "",
                releaseDate = "12-12-2021",
                bannerImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
                posterImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
                movieLink = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
                eventId = 1
            )
        )

        val fakeEvents = listOf(
            EventModel(
                id = 1,
                title = "Test Event 1",
                cover = "https://example.com/event1.jpg",
                genre = "Comedy",
                type = "Live Performance",
                duration = "120 min",
                director = "Jane Smith",
                description = "A fun and exciting live event.",
                eventLink = "https://example.com/event1",
                videoPlaceholderUrl = null,
                releaseDate = "2023-10-01",
                cinemas = emptyList(),
                location = "Brugge",
                movies = emptyList()

            )
        )

        val fakeMoviePosterRepo = FakeMoviePosterRepo(fakeMoviePosters)
        val fakeMovieRepo = FakeMovieRepo(fakeMovies)
        val fakeEventRepo = FakeEventRepo(fakeEvents)

        val homepageViewModel = HomepageViewModel(
            movieRepo = fakeMovieRepo,
            moviePosterRepo = fakeMoviePosterRepo,
            eventRepo = fakeEventRepo
        )

        advanceUntilIdle()

        val actualState = homepageViewModel.homepageUiState

        if (actualState is HomepageUiState.Succes) {
            Assert.assertEquals(fakeMoviePosters, actualState.recentMovies.value)
            Assert.assertEquals(fakeMovies, actualState.allMovies.value)
            Assert.assertEquals(fakeEvents, actualState.events.value)
        } else {
            Assert.fail("Expected HomepageUiState.Succes but got $actualState")
        }
    }
}

