package com.example.riseandroid.fake

import com.example.riseandroid.model.EventModel
import com.example.riseandroid.repository.IEventRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeEventRepo(private val events: List<EventModel>) : IEventRepo {
    override suspend fun getAllEventsList(): Flow<List<EventModel>> = flow { emit(events) }
    override suspend fun refreshEvents() {}
    override suspend fun getSpecificEvent(eventId: Int): EventModel? =
        events.find { it.id == eventId }
}
