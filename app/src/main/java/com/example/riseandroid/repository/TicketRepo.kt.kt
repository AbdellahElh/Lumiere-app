package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.Tickets.TicketDao
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.network.TicketApi
import com.example.riseandroid.util.asEntity
import com.example.riseandroid.util.asExternalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class TicketRepository(
    private val ticketApi: TicketApi,
    private val ticketDao: TicketDao,
    private val authrepo: IAuthRepo,
) : ITicketRepository {

    override suspend fun getTickets(): Flow<List<Ticket>> {
        return ticketDao.getTickets()
            .map { entities -> entities.map { it.asExternalModel() } }
            .onEach { tickets ->
                tickets.forEach { ticket ->
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

            val ticketsFromApi = ticketApi.getTickets()
            val ticketAsEntities = ticketsFromApi.map { it.asEntity() }
            ticketDao.deleteAllTickets()
            ticketDao.insertTickets(ticketAsEntities)

        } catch (e: Exception) {
            Log.e("TicketRepo", "Error refreshing tickets")
        }
    }

    override fun addTicket(
        movieCode: Int,
        eventCode: Int,
        cinemaName: String,
        showtime: LocalDateTime
    ): Flow<ApiResource<Ticket>> = flow {
    }

//        emit(ApiResource.Loading())
//        val newTicket = TicketEntity(
//            dateTime = showtime,
//            location = cinemaName,
//            type = TicketType.STANDAARD,
//            movieId = movieCode,
//            eventId = eventCode,
//            accountId = getLoggedInUserId()
//        )
//        try {
//            val response = ticketApi.addTicket(
//                movieId = movieCode,
//                eventId = eventCode,
//                cinemaName = cinemaName,
//                showtime = showtime
//            )
//            if (response.awaitResponse().isSuccessful) {
//                emit(ApiResource.Success(newTicket))
//                ticketDao.addTicket(newTicket)
//            } else {
//                emit(ApiResource.Error<TicketEntity>("Toevoegen gefaald"))
//                ticketDao.deleteTicket(newTicket)
//            }
//        } catch (e: Exception) {
//            emit(
//                ApiResource.Error<TicketEntity>(
//                    message = e.toString() ?: "Er was een onverwachte fout"
//                )
//            )
//            ticketDao.deleteTicket(newTicket)
//        }
//    }.flowOn(Dispatchers.IO)



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
