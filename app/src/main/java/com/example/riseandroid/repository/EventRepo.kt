package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.EventDao
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.network.EventsApi
import com.example.riseandroid.util.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
                    price = apiEvent.price ?: "0.00",
                    duration = apiEvent.duration ?: "0 minuten",
                    director = apiEvent.director ?: "Onbekend",
                    releaseDate = apiEvent.releaseDate ?: "Onbekend",
                    videoPlaceholderUrl = apiEvent.videoPlaceholderUrl,
                    cover = apiEvent.cover,
                    date = apiEvent.date,
                    location = apiEvent.location ?: "Onbekende locatie",
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

    override suspend fun getSpecificEvent(eventId: Int): EventModel? {
        return try {
            eventsApi.getSpecificEvent(eventId)
        } catch (e: Exception) {
            Log.e("EventRepo", "Error fetching specific event", e)
            null
        }
    }
}

