package com.example.riseandroid.dbTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.riseandroid.data.RiseDatabase
import com.example.riseandroid.data.entitys.CinemaEntity
import com.example.riseandroid.data.entitys.MovieDao
import com.example.riseandroid.data.entitys.ShowtimeEntity
import com.example.riseandroid.mockdata.MovieListMock
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    private lateinit var movieDao: MovieDao
    private lateinit var risedb: RiseDatabase

    private val movieEntity = MovieListMock().loadMovieEntitiesMock().first()


    private val testCinemaEntity = CinemaEntity(
        id = 201,
        name = "Test Cinema",
    )

    private val testShowtimeEntity = ShowtimeEntity(
        id = 301,
        movieId = 101,
        cinemaId = 201,
        showDate = "2024-01-01",
        showTime = "18:00"
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        risedb = Room.inMemoryDatabaseBuilder(context, RiseDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        movieDao = risedb.movieDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        risedb.close()
    }

    @Test
    fun insertAndGetMovies() = runBlocking {
        // Insert required data for the query
        movieDao.insertCinema(testCinemaEntity) // Insert a cinema
        movieDao.insertMovie(movieEntity)       // Insert a movie
        movieDao.insertShowtimes(listOf(testShowtimeEntity)) // Associate the movie with the cinema via showtimes

        // Fetch movies
        val movies = movieDao.getAllMovies().first()

        // Assertions
        assertEquals(1, movies.size)
        assertEquals(movieEntity.id, movies.first().id)
        assertEquals(movieEntity.title, movies.first().title)
    }


    @Test
    fun deleteMovie() = runBlocking {
        // Insert required data
        movieDao.insertCinema(testCinemaEntity)
        movieDao.insertMovie(movieEntity)
        movieDao.insertShowtimes(listOf(testShowtimeEntity))

        // Verify insertion
        var movies = movieDao.getAllMovies().first()
        assertEquals(1, movies.size)

        // Delete the movie
        movieDao.deleteMovie(movieEntity)

        // Verify deletion
        movies = movieDao.getAllMovies().first()
        assertTrue(movies.isEmpty())
    }


    @Test
    fun insertAndGetCinemasByMovieId() = runBlocking {
        movieDao.insertCinema(testCinemaEntity)
        movieDao.insertMovie(movieEntity)
        movieDao.insertShowtimes(listOf(testShowtimeEntity))

        val cinemas = movieDao.getCinemasByMovieId(movieEntity.id)
        assertEquals(1, cinemas.size)
        assertEquals(testCinemaEntity.id, cinemas.first().id)
    }

    @Test
    fun getShowtimesByMovieAndCinema() = runBlocking {
        movieDao.insertCinema(testCinemaEntity)
        movieDao.insertMovie(movieEntity)
        movieDao.insertShowtimes(listOf(testShowtimeEntity))

        val showtimes = movieDao.getShowtimesByMovieAndCinema(
            movieId = movieEntity.id,
            cinemaId = testCinemaEntity.id
        )
        assertEquals(1, showtimes.size)
        assertEquals(testShowtimeEntity.id, showtimes.first().id)
    }

    @Test
    fun getFilteredMoviesByCinemaAndDate() = runBlocking {
        movieDao.insertCinema(testCinemaEntity)
        movieDao.insertMovie(movieEntity)
        movieDao.insertShowtimes(listOf(testShowtimeEntity))

        val moviesFlow = movieDao.getFilteredMoviesByCinemaAndDate(
            selectedDate = "2024-01-01",
            selectedCinemas = listOf(testCinemaEntity.name),
            searchTitle = "%Test%"
        )
        val movies = moviesFlow.first()
        assertEquals(1, movies.size)
        assertEquals(movieEntity.id, movies.first().id)
    }
}