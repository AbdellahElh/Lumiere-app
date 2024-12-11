package com.example.riseandroid.fake


import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.riseandroid.data.entitys.CinemaEntity
import com.example.riseandroid.data.entitys.EventDao
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

 class FakeEventDao:EventDao {
    override fun getAllEvents(): Flow<List<EventEntity>> {
        return flowOf(events)
    }

    override suspend fun getEventById(eventId: Int): EventEntity? {
        return events.find { it.id == eventId }

    }
    override suspend fun insertEvents(events: List<EventEntity>){
        this.events.addAll(events)

    }








    private val events = mutableListOf(
        EventEntity(
            id = 1,

            location = "BRugge",
            title = "title1",
            genre = "action",
            type = "marathon",
            description = "description",
            duration = 225,
            director = "john",
            releaseDate = "25-04-2024",
            videoPlaceholderUrl = "fdsfdsf",
            cover = "fsdfds",
            eventLink = "sfdsf",
            cinemasJson = "sdfdsf",
            moviesJson = "sdfsdf"
        ),
        EventEntity(
            id = 2,

            location = "BRugge",
            title = "title2",
            genre = "action",
            type = "marathon",
            description = "description",
            duration = 225,
            director = "john",
            releaseDate = "25-04-2024",
            videoPlaceholderUrl = "fdsfdsf",
            cover = "fsdfds",
            eventLink = "sfdsf",
            cinemasJson = "sdfdsf",
            moviesJson = "sdfsdf"
        ),
    )

}