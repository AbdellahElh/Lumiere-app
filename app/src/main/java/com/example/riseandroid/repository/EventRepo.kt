package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.EventDao
import com.example.riseandroid.data.response.EventResponse
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.network.EventsApi
import com.example.riseandroid.util.asExternalModel
import com.example.riseandroid.util.toDomainModel
import com.example.riseandroid.util.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface IEventRepo {
    suspend fun getAllEventsList(): Flow<List<EventModel>>
    suspend fun refreshEvents()
    suspend fun getSpecificEvent(eventId: Int): EventModel?
}

class EventRepo(
    private val eventDao: EventDao,
    private val eventsApi: EventsApi
) : IEventRepo {

    override suspend fun getAllEventsList(): Flow<List<EventModel>> {
        return eventDao.getAllEvents()
            .map { entities ->
                entities.map { it.asExternalModel() }
            }
            .onStart {
                refreshEvents()
            }
    }

    override suspend fun refreshEvents() {
        try {
            val eventsFromApi: List<EventResponse> = eventsApi.getAllEvents()
            val eventsAsEntities = eventsFromApi.map { it.toEntity() }
            eventDao.insertEvents(eventsAsEntities)
        } catch (e: Exception) {
            Log.e("EventRepo", "Error refreshing events", e)
        }
    }

    override suspend fun getSpecificEvent(eventId: Int): EventModel? {
        return try {
            val localEvent = eventDao.getEventById(eventId)?.asExternalModel()

            localEvent ?: eventsApi.getSpecificEvent(eventId).toDomainModel()
        } catch (e: Exception) {
            Log.e("EventRepo", "Error fetching specific event", e)
            null
        }
    }
}





