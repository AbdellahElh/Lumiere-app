package com.example.riseandroid.repository

import android.util.Log
import com.example.riseandroid.data.entitys.EventDao
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.network.EventsApi
import com.example.riseandroid.util.asEntity
import com.example.riseandroid.util.toDomainModel
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
            emit(eventsFromApi.map { it.toDomainModel() })
        } catch (e: Exception) {
            Log.e("EventRepo", "Error fetching events from API", e)
            emit(emptyList())
        }
    }

    override suspend fun refreshEvents() {
        try {
            val eventsFromApi = eventsApi.getAllEvents()
            val eventsAsEntities = eventsFromApi.map { it.toDomainModel().asEntity() }
            eventDao.insertEvents(eventsAsEntities)
        } catch (e: Exception) {
            Log.e("EventRepo", "Error refreshing events", e)
        }
    }

    override suspend fun getSpecificEvent(eventId: Int): EventModel? {
        return try {
            eventsApi.getSpecificEvent(eventId).toDomainModel()
        } catch (e: Exception) {
            Log.e("EventRepo", "Error fetching specific event", e)
            null
        }
    }
}


