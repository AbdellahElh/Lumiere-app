package com.example.riseandroid.fake.ticket

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.riseandroid.data.entitys.CinemaEntity
import com.example.riseandroid.data.entitys.EventEntity
import com.example.riseandroid.data.entitys.MovieDao
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.ShowtimeEntity
import com.example.riseandroid.data.entitys.Tickets.TicketDao
import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.data.response.TicketResponse
import com.example.riseandroid.model.Ticket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTicketDao:TicketDao {
    override fun getTickets(): Flow<List<TicketEntity>> {
        return flowOf(tickets)
    }
    override suspend fun insertTickets(tickets: List<TicketEntity>)
    {
        this.tickets.addAll(tickets)
    }

    override suspend fun insertEvent(event: EventEntity){
    }

    override suspend fun insertMovie(movie: MovieEntity){
    }

    override suspend fun addTicket(ticket: TicketEntity){
        tickets.add(ticket)
    }


    override suspend fun deleteTicket(ticket: TicketEntity){
        tickets.remove(ticket)
    }


    override suspend fun deleteAllTickets()    {
        tickets.clear()
    }






    private val tickets = mutableListOf(
        TicketEntity(
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
        TicketEntity(
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

}