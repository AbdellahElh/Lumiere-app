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
}

class EventRepo(
    private val eventDao: EventDao,
    private val eventsApi: EventsApi
) : IEventRepo {

    override suspend fun getAllEventsList(): Flow<List<EventModel>> = flow {
        try {
            val eventsFromApi = eventsApi.getAllEvents()
            emit(eventsFromApi)
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
}

