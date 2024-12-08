package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.EventDao
import com.example.riseandroid.data.entitys.MovieDao
import com.example.riseandroid.data.entitys.Tickets.TicketDao
import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.data.entitys.Tickets.TicketType
import com.example.riseandroid.data.response.EventResponse
import com.example.riseandroid.data.response.TicketResponse
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.network.EventsApi
import com.example.riseandroid.network.MoviesApi
import com.example.riseandroid.network.TicketApi
import com.example.riseandroid.util.asEntity
import com.example.riseandroid.util.asExternalModel
import com.example.riseandroid.util.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class TicketRepository(
    private val ticketApi: TicketApi,
    private val ticketDao: TicketDao,
    private val authrepo: IAuthRepo,
    private val movieApi: MovieDao,
    private val EventApi : EventDao
) : ITicketRepository {

    override suspend fun getTickets(): Flow<List<Ticket>> {
        return ticketDao.getTickets()
            .map { entities -> entities.map { it.asExternalModel() } }
            .onEach { tickets ->
                tickets.forEach { ticket ->
                    saveMovieEvent(ticket)
                    Log.d("TicketList", ticket.toString())
                }
            }
            .onStart {
                withContext(Dispatchers.IO) {
                    refreshTickets()
                }
            }
    }

    private suspend fun refreshTickets() {
        try {

            val ticketsFromApi: List<TicketResponse> = ticketApi.getTickets()
            val ticketAsEntities = ticketsFromApi.map { it.asEntity() }

            ticketDao.deleteAllTickets()
            ticketDao.insertTickets(ticketAsEntities)

        } catch (e: Exception) {
            Log.e("TicketRepo", "Error refreshing tickets" + e)
        }
    }
    private suspend fun saveMovieEvent(ticket: Ticket) {

        if(ticket.eventId != null){
            val event = EventApi.getEventById(ticket.eventId)
            val eventEntity = event
            if (eventEntity != null) {
                ticketDao.insertEvent(eventEntity)
            }
        }
        else{
            val movie = ticket.movieId?.let { movieApi.getMovieById(it) }
            val movieEntity = movie
            if (movie != null) {
                if (movieEntity != null) {
                    ticketDao.insertMovie(movieEntity)
                }
            }
        }

    }
    override suspend fun addTicket(
        movieCode: Int,
        eventCode: Int,
        cinemaName: String,
        showtime: String
    ): TicketEntity {
        return withContext(Dispatchers.IO) {
            val newTicket = TicketEntity(
                dateTime = showtime,
                location = cinemaName,
                type = 0,
                movieId = movieCode,
                eventId = eventCode,
                accountId = getLoggedInUserId()
            )

            try {
                val response = ticketApi.addTicket(
                    movieId = movieCode,
                    eventId = eventCode,
                    cinemaName = cinemaName,
                    showtime = showtime
                )
                if (response.awaitResponse().isSuccessful) {
                    ticketDao.addTicket(newTicket)
                    newTicket
                } else {
                    ticketDao.deleteTicket(newTicket)
                    throw Exception("Failed to add ticket on the server")
                }
            } catch (e: Exception) {
                ticketDao.deleteTicket(newTicket)
                throw e
            }
        }
    }




    suspend fun getLoggedInUserId(): Int {
        var accountId: Int? = null
        authrepo.getLoggedInId().collect { resource ->
            when (resource) {
                is ApiResource.Error -> throw IllegalStateException("De gebruiker moet ingelogd zijn")
                is ApiResource.Initial -> throw IllegalStateException("Unexpected state: ApiResource.Initial")
                is ApiResource.Loading -> {
                    // Optionally handle the loading state, or simply ignore it.
                }

                is ApiResource.Success -> {
                    accountId = resource.data
                }
            }
        }
                return accountId ?: throw IllegalStateException("De gebruiker moet ingelogd zijn")
    }
}
