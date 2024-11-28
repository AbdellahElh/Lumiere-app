// HomepageViewModelTest.kt
package com.example.riseandroid

import com.example.riseandroid.fake.FakeMoviePosterRepo
import com.example.riseandroid.fake.FakeMovieRepo
import com.example.riseandroid.fake.FakeProgramRepository
import com.example.riseandroid.model.Cinema
import com.example.riseandroid.model.Movie
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.model.Program
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
    fun homepageViewModel_getMoviePosters_verifyHomepageUiStateSuccess() = runTest {
        // Arrange: Set up the fake movie posters and repository
        val fakeMoviePosters = listOf(
            MoviePoster(
                id = 1,
                cover = "https://example.com/poster1.jpg",
                releaseDate = "2023-10-01T00:00:00"
            )
        )

        val fakeMovies = listOf(
            Movie(
                movieId = 1L,
                title = "Test Movie 1",
                posterResourceId = R.drawable.screenshot_2024_10_08_105150,
                description = "An action-packed adventure.",
                genre = "Action",
                length = "120 min",
                director = "John Doe"
            ),
            Movie(
                movieId = 2L,
                title = "Test Movie 2",
                posterResourceId = R.drawable.screenshot_2024_10_10_102504,
                description = "A hilarious journey.",
                genre = "Comedy",
                length = "90 min",
                director = "Jane Smith"
            )
        )

        val fakeMovieModels = listOf(
            MovieModel(
                id = 1,
                title = "Test Movie",
                coverImageUrl = "https://m.media-amazon.com/images/I/613ypTLZHsL._AC_UF1000,1000_QL80_.jpg",
                genre = "Action",
                duration = "120 min",
                director = "John Doe",
                description = "An action-packed adventure.",
                video = null,
                videoPlaceholderUrl = null,
                cast = listOf("Actor 1", "Actor 2"),
                cinemas = listOf(
                    Cinema(
                        id = 1,
                        name = "Brugge Cinema",
                        showtimes = listOf("2023-10-01T18:00:00", "2023-10-01T20:00:00")
                    )
                )
            )
        )

        val fakePrograms = listOf(
            Program(
                date = "2023-10-01",
                movie = fakeMovies[0],
                hours = "18:00",
                location = "Brugge"
            )
        )

        val fakeMoviePosterRepo = FakeMoviePosterRepo(fakeMoviePosters)
        val fakeMovieRepo = FakeMovieRepo(fakeMovieModels)
        val fakeProgramRepository = FakeProgramRepository(fakePrograms)

        // Act: Create the ViewModel with the fake repositories
        val homepageViewModel = HomepageViewModel(
//            programRepository = fakeProgramRepository,
            movieRepo = fakeMovieRepo,
            moviePosterRepo = fakeMoviePosterRepo
        )

        // Advance the coroutine until all tasks are completed
        advanceUntilIdle()

        // Assert: Verify the UI state is Success and contains the expected movie posters
        val actualState = homepageViewModel.homepageUiState // Access directly without .value

        if (actualState is HomepageUiState.Success) {
            // Access the StateFlows and collect their current values
            val recentMovies = actualState.recentMovies.value
            val allMovies = actualState.allMovies.value
//            val programFilms = actualState.nonRecentMovies.value

            // Assert that the recent movies match the fake data
            Assert.assertEquals(fakeMoviePosters, recentMovies)

            // Optionally, assert other properties
            Assert.assertEquals(fakeMovieModels, allMovies)
//            Assert.assertEquals(fakePrograms, programFilms)
        } else {
            Assert.fail("Expected HomepageUiState.Success but got $actualState")
        }
    }
}
