package com.example.riseandroid.dbTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.riseandroid.data.RiseDatabase
import com.example.riseandroid.data.entitys.Tickets.TicketDao
import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.data.entitys.MovieEntity
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
class TicketDaoTest {

    private lateinit var ticketDao: TicketDao
    private lateinit var risedb: RiseDatabase

    private val testMovieEntity = MovieEntity(
        id = 101,
        title = "Test Movie",
        genre = "Action",
        description = "A test movie",
        duration = 120,
        releaseDate = "2021-01-01",
        director = "Test Director",
        videoPlaceholderUrl = null,
        coverImageUrl = null,
        bannerImageUrl = null,
        posterImageUrl = null,
        movieLink = "https://example.com/movie",
        eventId = null
    )

    private val testTicketEntity = TicketEntity(
        id = 1,
        dateTime = "2024-12-25T18:30:00",
        location = "Ghent",
        type = 0,
        movieId = 101,
        eventId = null,
        accountId = 1
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        risedb = Room.inMemoryDatabaseBuilder(context, RiseDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        ticketDao = risedb.ticketDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        risedb.close()
    }

    @Test
    fun addAndGetTickets() = runBlocking {
        risedb.movieDao().insertMovie(testMovieEntity)

        ticketDao.addTicket(testTicketEntity)

        val tickets = ticketDao.getTickets().first()
        assertEquals(1, tickets.size)
        assertEquals(testTicketEntity.id, tickets.first().id)
        assertEquals(testTicketEntity.movieId, tickets.first().movieId)
    }

    @Test
    fun deleteTicket() = runBlocking {
        risedb.movieDao().insertMovie(testMovieEntity)

        ticketDao.addTicket(testTicketEntity)

        var tickets = ticketDao.getTickets().first()
        assertEquals(1, tickets.size)

        ticketDao.deleteTicket(testTicketEntity)

        tickets = ticketDao.getTickets().first()
        assertTrue(tickets.isEmpty())
    }

    @Test
    fun deleteAllTickets() = runBlocking {
        risedb.movieDao().insertMovie(testMovieEntity)

        ticketDao.addTicket(testTicketEntity)

        var tickets = ticketDao.getTickets().first()
        assertEquals(1, tickets.size)

        ticketDao.deleteAllTickets()

        tickets = ticketDao.getTickets().first()
        assertTrue(tickets.isEmpty())
    }
}
