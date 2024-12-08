package com.example.riseandroid

import com.example.riseandroid.fake.FakeAuth0Repo
import com.example.riseandroid.fake.movies.FakeMovieDao
import com.example.riseandroid.fake.ticket.FakeTicketApi
import com.example.riseandroid.fake.ticket.FakeTicketDao
import com.example.riseandroid.repository.Auth0Repo
import com.example.riseandroid.repository.IAuthRepo
import com.example.riseandroid.repository.ITicketRepository
import com.example.riseandroid.repository.TicketRepository
import com.example.riseandroid.rules.TestDispatcherRule
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

class TicketRepoTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var ticketRepo: ITicketRepository
    private val ticketDao = FakeTicketDao()
    private val ticketApi = FakeTicketApi()
    private val movieDao = FakeMovieDao()
    private val authRepo =  FakeAuth0Repo()


    @Before
    fun setUp() {
        ticketRepo = TicketRepository(
            ticketApi,
            ticketDao = ticketDao,
            authrepo = authRepo,
            movieApi = movieDao,
            EventApi = null
        )
    }
    @Test
    fun testGetTickets() = runTest {
        val tickets = ticketRepo.getTickets().toList()[0]
        assertNotNull(tickets)
        assertEquals(2, tickets.size)
    }

    @Test
    fun testAddTicket() = runTest {
        val newTicket = ticketRepo.addTicket(
            movieCode = 4,
            eventCode = 0,
            cinemaName = "Gent",
            showtime = "2024-12-31T20:00:00"
        )
        assertNotNull(newTicket)
        assertEquals(3, newTicket.id)
    }





}
