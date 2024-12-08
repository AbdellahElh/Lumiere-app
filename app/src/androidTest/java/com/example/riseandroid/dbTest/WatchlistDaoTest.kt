package com.example.riseandroid.dbTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.riseandroid.data.RiseDatabase
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.watchlist.MovieWatchlistEntity
import com.example.riseandroid.data.entitys.watchlist.WatchlistDao
import com.example.riseandroid.data.entitys.watchlist.WatchlistEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WatchlistDaoTest {

    private lateinit var watchlistDao: WatchlistDao
    private lateinit var risedb: RiseDatabase

    private val testUserId = 123
    private val testWatchlistEntity = WatchlistEntity(
        id = 1,
        userId = testUserId
    )

    private val testMovieEntity = MovieEntity(
        id = 101,
        title = "Test Movie",
        genre = "Action",
        description = "A test movie",
        duration = 120,
        releaseDate = "2021-01-01",
        director = "Test Director",
        videoPlaceholderUrl = "https://example.com/video",
        coverImageUrl = "https://example.com/cover",
        bannerImageUrl = "https://example.com/banner",
        posterImageUrl = "https://example.com/poster",
        movieLink = "https://example.com/movie",
        eventId = 1
    )

    private val secondTestMovieEntity = MovieEntity(
        id = 102,
        title = "Test Movie",
        genre = "Action",
        description = "A test movie",
        duration = 120,
        releaseDate = "2021-01-01",
        director = "Test Director",
        videoPlaceholderUrl = "https://example.com/video",
        coverImageUrl = "https://example.com/cover",
        bannerImageUrl = "https://example.com/banner",
        posterImageUrl = "https://example.com/poster",
        movieLink = "https://example.com/movie",
        eventId = 2
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        risedb = Room.inMemoryDatabaseBuilder(context, RiseDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        watchlistDao = risedb.watchlistDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        risedb.close()
    }

    @Test
    fun addAndGetMoviesInWatchlist() = runBlocking {
        watchlistDao.insertWatchlist(testWatchlistEntity)

        watchlistDao.addMovieToWatchlist(testMovieEntity)

        val watchlistId = watchlistDao.getWatchlistIdForUser(testUserId)
        assertNotNull(watchlistId)

        watchlistDao.addToWatchlist(
            MovieWatchlistEntity(
                movieId = testMovieEntity.id,
                watchlistId = watchlistId!!
            )
        )

        val moviesFlow = watchlistDao.getMoviesInWatchlist(testUserId)
        val movies = moviesFlow.first()
        assertEquals(1, movies.size)
        assertEquals(testMovieEntity.id, movies.first().id)
        assertEquals(testMovieEntity.title, movies.first().title)
    }

    @Test
    fun removeMovieFromWatchlist() = runBlocking {
        watchlistDao.insertWatchlist(testWatchlistEntity)
        val watchlistId = watchlistDao.getWatchlistIdForUser(testUserId)
        assertNotNull(watchlistId)

        watchlistDao.insertMovies(listOf(testMovieEntity, secondTestMovieEntity))

        val relations = listOf(
            MovieWatchlistEntity(movieId = testMovieEntity.id, watchlistId = watchlistId!!),
            MovieWatchlistEntity(movieId = secondTestMovieEntity.id, watchlistId = watchlistId)
        )
        watchlistDao.insertMovieWatchlistRelations(relations)

        var movies = watchlistDao.getMoviesInWatchlist(testUserId).first()
        assertEquals(2, movies.size)

        watchlistDao.removeFromWatchlist(testMovieEntity.id, testUserId)

        movies = watchlistDao.getMoviesInWatchlist(testUserId).first()
        assertEquals(1, movies.size)
        assertEquals(secondTestMovieEntity.id, movies.first().id)
    }

    @Test
    fun clearWatchlistForUser() = runBlocking {
        watchlistDao.insertWatchlist(testWatchlistEntity)
        val watchlistId = watchlistDao.getWatchlistIdForUser(testUserId)
        assertNotNull(watchlistId)

        watchlistDao.insertMovies(listOf(testMovieEntity, secondTestMovieEntity))
        val relations = listOf(
            MovieWatchlistEntity(movieId = testMovieEntity.id, watchlistId = watchlistId!!),
            MovieWatchlistEntity(movieId = secondTestMovieEntity.id, watchlistId = watchlistId)
        )
        watchlistDao.insertMovieWatchlistRelations(relations)

        var movies = watchlistDao.getMoviesInWatchlist(testUserId).first()
        assertEquals(2, movies.size)

        watchlistDao.clearWatchlistForUser(testUserId)

        movies = watchlistDao.getMoviesInWatchlist(testUserId).first()
        assertTrue(movies.isEmpty())
    }

    @Test
    fun clearAllWatchlistData() = runBlocking {
        watchlistDao.insertWatchlist(testWatchlistEntity)
        val watchlistId = watchlistDao.getWatchlistIdForUser(testUserId)
        assertNotNull(watchlistId)

        watchlistDao.addMovieToWatchlist(testMovieEntity)
        watchlistDao.addToWatchlist(
            MovieWatchlistEntity(movieId = testMovieEntity.id, watchlistId = watchlistId!!)
        )

        var movies = watchlistDao.getMoviesInWatchlist(testUserId).first()
        assertEquals(1, movies.size)

        watchlistDao.clearAllWatchlistData()

        movies = watchlistDao.getMoviesInWatchlist(testUserId).first()
        assertTrue(movies.isEmpty())
    }
}
