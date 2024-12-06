package com.example.riseandroid.fake

import com.example.riseandroid.model.EventModel
import com.example.riseandroid.repository.IEventRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeEventRepo : IEventRepo {

    override suspend fun getAllEventsList(): Flow<List<EventModel>> {
        return flowOf(
            listOf(
                EventModel(
                    id = 1,
                    title = "Event 1",
                    genre = "Comedy",
                    type = "Show",
                    description = "Description of Event 1",
                    duration = "90 min",
                    director = "Director 1",
                    releaseDate = "2024-12-01",
                    videoPlaceholderUrl = null,
                    cover = null,
                    location = "Location 1",
                    eventLink = null,
                    cinemas = emptyList()
                ),
                EventModel(
                    id = 2,
                    title = "Event 2",
                    genre = "Drama",
                    type = "Movie",
                    description = "Description of Event 2",
                    duration = "120 min",
                    director = "Director 2",
                    releaseDate = "2024-12-02",
                    videoPlaceholderUrl = null,
                    cover = null,
                    location = "Location 2",
                    eventLink = null,
                    cinemas = emptyList()
                )
            )
        )
    }

    override suspend fun refreshEvents() {
        // No-op
    }

    override suspend fun getSpecificEvent(eventId: Int): EventModel? {
        return null // Niet nodig voor simpele tests
    }
}


