package com.example.riseandroid.fake

import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.TenturncardEntity
import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.data.entitys.event.AddTicketDTO
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.ITenturncardRepository
import com.example.riseandroid.repository.ITicketRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTicketRepository : ITicketRepository {

    private var fakeTickets = mutableListOf(
        Ticket(
            id = 1,
            dateTime = "2025-01-01T00:00:00",
            location = "BRugge",
            type = 0,
            movieId = 1,
            eventId = null,
            accountId = 1,
            movie = MovieEntity(
                id = 1,
                eventId = null,
                title = "Movie Title 1",
                genre = "action",
                description = "descirption",
                duration = 512,
                releaseDate = "15-20-2024",
                director = "ford",
                videoPlaceholderUrl = null,
                coverImageUrl = null,
                bannerImageUrl = null,
                posterImageUrl = null,
                movieLink = "movielink",
            ),
            event = null,
        ),
        Ticket(
            id = 2,
            dateTime = "2024-12-25T18:30:00",
            location = "BRugge",
            type = 0,
            movieId = 1,
            eventId = null,
            accountId = 1,
            movie = MovieEntity(
                id = 1,
                eventId = null,
                title = "Movie Title 2",
                genre = "action",
                description = "descirption",
                duration = 512,
                releaseDate = "15-20-2024",
                director = "ford",
                videoPlaceholderUrl = null,
                coverImageUrl = null,
                bannerImageUrl = null,
                posterImageUrl = null,
                movieLink = "movielink",
            ),
            event = null,
        ),
        Ticket(
            id = 3,
            dateTime = "2025-03-15T14:45:30",
            location = "BRugge",
            type = 0,
            movieId = 1,
            eventId = null,
            accountId = 1,
            movie = MovieEntity(
                id = 1,
                eventId = null,
                title = "film1",
                genre = "action",
                description = "descirption",
                duration = 512,
                releaseDate = "15-20-2024",
                director = "ford",
                videoPlaceholderUrl = null,
                coverImageUrl = null,
                bannerImageUrl = null,
                posterImageUrl = null,
                movieLink = "movielink",
            ),
            event = null,
        ),
    )

    override suspend fun getTickets(): Flow<List<Ticket>> {
        return flow {
            delay(1000)
            emit(fakeTickets)
        }
    }

    override suspend fun addTicket(
        ticket : AddTicketDTO
    ) {
        val newTicket = Ticket(
            id = fakeTickets.size + 1,
            dateTime = ticket.ShowTime,
            location = ticket.CinemaName,
            type = 0,
            movieId = ticket.MovieId,
            eventId = ticket.EventId,
            accountId = 1
        )
        fakeTickets.add(newTicket)
    }
}


