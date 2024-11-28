package com.example.riseandroid.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.riseandroid.data.entitys.EventDao
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.network.EventsApi
import com.example.riseandroid.util.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface IEventRepo {
    suspend fun getAllEventsList(): Flow<List<EventModel>>
    suspend fun refreshEvents()
    suspend fun getSpecificEvent(eventId: Int): EventModel?
}

class EventRepo(
    private val eventDao: EventDao,
    private val eventsApi: EventsApi
) : IEventRepo {

    override suspend fun getAllEventsList(): Flow<List<EventModel>> = flow {
        try {
            val eventsFromApi = eventsApi.getAllEvents()
            emit(eventsFromApi.map { apiEvent ->
                EventModel(
                    id = apiEvent.id,
                    title = apiEvent.title ?: "Geen titel",
                    genre = apiEvent.genre ?: "Onbekend genre",
                    type = apiEvent.type ?: "Onbekend type",
                    description = apiEvent.description ?: "Geen beschrijving",
                    duration = apiEvent.duration ?: "0 minuten",
                    director = apiEvent.director ?: "Onbekend",
                    releaseDate = apiEvent.releaseDate ?: "Onbekend",
                    videoPlaceholderUrl = apiEvent.videoPlaceholderUrl,
                    cover = apiEvent.cover,
                    location = apiEvent.location ?: "Onbekend",
                    eventLink = apiEvent.eventLink ?: "",
                    cinemas = apiEvent.cinemas ?: emptyList()
                )
            })
        } catch (e: Exception) {
            Log.e("EventRepo", "Error fetching events from API", e)
            emit(emptyList())
        }
    }


    override suspend fun refreshEvents() {
        try {
            val eventsFromApi = eventsApi.getAllEvents()
            val eventsAsEntities = eventsFromApi.map { it.asEntity() }
            eventDao.insertEvents(eventsAsEntities)
        } catch (e: Exception) {
            Log.e("EventRepo", "Error refreshing events", e)
        }
    }

    @SuppressLint("NewApi")
    override suspend fun getSpecificEvent(eventId: Int): EventModel? {
        return try {
            val eventFromApi = eventsApi.getSpecificEvent(eventId)
            eventFromApi.copy(
                cinemas = eventFromApi.cinemas.map { cinema ->
                    cinema.copy(
                        showtimes = cinema.showtimes.map { showtime ->
                            LocalDateTime.parse(showtime, DateTimeFormatter.ISO_DATE_TIME)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        }
                    )
                }
            )
        } catch (e: Exception) {
            Log.e("EventRepo", "Error fetching specific event", e)
            null
        }
    }
}

