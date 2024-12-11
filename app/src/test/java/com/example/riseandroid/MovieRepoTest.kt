package com.example.riseandroid

import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.fake.movies.FakeMovieApi
import com.example.riseandroid.fake.movies.FakeMovieDao
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.repository.IMovieRepo
import com.example.riseandroid.repository.MovieRepo
import com.example.riseandroid.rules.TestDispatcherRule
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        duration = 120,
        director = "Director Name",
        description = "Test Description",
        videoPlaceholderUrl = "video_url",
        eventId = 1,
        bannerImageUrl = "banner",
        posterImageUrl = "poster",
        movieLink = "movieLink",
        releaseDate = "2023-10-01"
    )

    private val movieModel = MovieModel(
        id = 1,
        title = "Test Movie",
        cinemas = emptyList(),
        cast = emptyList(),
        coverImageUrl = "https://cdn.atwilltech.com/flowerdatabase/p/perfect-love-bouquet-fresh-flowers-VA00707.425.jpg",
        genre = "",
        duration = 120,
        director = "",
        description = "",
        videoPlaceholderUrl = "",
        releaseDate = "12-12-2021",
        bannerImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
        posterImageUrl = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
        movieLink = "https://i.pinimg.com/736x/2e/cf/06/2ecf067a2069128f44d75d25a32e219e.jpg",
        eventId = 1
    )


}
