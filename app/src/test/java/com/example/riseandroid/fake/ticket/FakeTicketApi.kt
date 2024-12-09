package com.example.riseandroid.fake.ticket

import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.data.entitys.event.AddTicketDTO
import com.example.riseandroid.data.response.TicketResponse
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.network.TicketApi
import retrofit2.Call
import retrofit2.mock.Calls

class FakeTicketApi : TicketApi {

    private val dummyTickets = listOf(
        TicketResponse(
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
        TicketResponse(
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
    )

    override suspend fun getTickets(): List<TicketResponse> {
        return dummyTickets
    }

    override suspend fun addTicket(
        ticket : AddTicketDTO

    ): TicketEntity {
        val newTicket = TicketEntity(
            id = dummyTickets.size + 1,
            movieId = ticket.MovieId,
            eventId = ticket.EventId,
            dateTime = ticket.ShowTime,
            location = "BRugge",
            type = 0,
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
            )
        )
        return newTicket
    }
}
