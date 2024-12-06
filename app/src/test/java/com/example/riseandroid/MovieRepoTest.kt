package com.example.riseandroid

import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.fake.movies.FakeMovieApi
import com.example.riseandroid.fake.movies.FakeMovieDao
import com.example.riseandroid.repository.MovieRepo
import com.example.riseandroid.repository.IMovieRepo
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.rules.TestDispatcherRule
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

class MovieRepoTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var movieRepo: IMovieRepo
    private val movieDao = FakeMovieDao()
    private val movieApi = FakeMovieApi()


    @Before
    fun setUp() {
        movieRepo = MovieRepo(movieDao, movieApi)
    }

    @Test
    fun testGetAllMoviesList() = runTest {
        val selectedDate = "2024-12-05"
        val selectedCinemas = listOf("Cinema 1", "Cinema 2")
        val searchTitle = "Test Movie"


        val resultFlow = movieRepo.getAllMoviesList(selectedDate, selectedCinemas, searchTitle)

        val resultList = resultFlow.toList()


        assertNotNull(resultList)
        assertEquals(1, resultList.size)
    }

    @Test
    fun testGetMovieById_FromDao() = runTest {

        movieDao.insertMovie(movieEntity)

        val movie = movieRepo.getMovieById(1)


        assertEquals(movieModel.id, movie.id)
        assertEquals(movieModel.title, movie.title)
        assertEquals(movieModel.duration, movie.duration)
    }

    private val movieEntity = MovieEntity(
        id = 1,
        title = "Test Movie",
        coverImageUrl = "test_url",
        genre = "Drama",
        duration = "120 min",
        director = "Director Name",
        description = "Test Description",
        videoPlaceholderUrl = "video_url",
        eventId = 1,
        bannerImageUrl = "banner",
        posterImageUrl = "poster",
        movieLink = "movieLink",
    )

    private val movieModel = MovieModel(
        id = 1,
        title = "Test Movie",
        coverImageUrl = "test_url",
        genre = "Drama",
        duration = "120 min",
        director = "Director Name",
        description = "Test Description",
        video = "video_url",
        videoPlaceholderUrl = "placeholder_url",
        cast = emptyList(),
        cinemas = emptyList(),
        eventId = 1,
    )


}
