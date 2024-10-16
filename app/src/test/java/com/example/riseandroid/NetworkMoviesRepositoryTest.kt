package com.example.riseandroid


import com.example.riseandroid.data.lumiere.NetworkMoviesRepository
import com.example.riseandroid.fake.FakeDataSource
import com.example.riseandroid.fake.FakeLumiereApiService
import com.example.riseandroid.rules.TestDispatcherRule
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class NetworkMoviesRepositoryTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun networkMoviesRepository_getRecentMovies_verifyMoviesList() =
        runTest {
            val repository = NetworkMoviesRepository(
                lumiereApiService = FakeLumiereApiService()
            )
            Assert.assertEquals(
                listOf(FakeDataSource.LoadRecentMoviesMock()).asFlow(),
                repository.getRecentMovies())
        }

    @Test
    fun networkMoviesRepository_getNonRecentMovies_verifyMoviesList() =
        runTest {
            val repository = NetworkMoviesRepository(
                lumiereApiService = FakeLumiereApiService()
            )
            Assert.assertEquals(
                listOf(FakeDataSource.LoadNonRecenMoviesMock()).asFlow(),
                repository.getNonRecentMovies()
            )
        }
}