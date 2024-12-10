package com.example.riseandroid.mockdata

import com.example.riseandroid.data.entitys.Cinema
import com.example.riseandroid.model.EventModel

class EventListMock {
    fun LoadAllEventsMock(): List<EventModel> {
        return listOf(
            EventModel(
                id = 1,
                title = "Test Event 1",
                cover = "https://example.com/event1.jpg",
                genre = "Comedy",
                type = "Live Performance",
                duration = "120 min",
                director = "Jane Smith",
                description = "A fun and exciting live event.",
                eventLink = "https://example.com/event1",
                videoPlaceholderUrl = null,
                releaseDate = "2023-10-01",
                cinemas = listOf(
                    Cinema(
                        id = 1,
                        name = "Brugge Cinema",
                        showtimes = listOf("2023-10-01T18:00:00", "2023-10-01T20:00:00")
                    )
                ),
                location = "Brugge",
                movies = emptyList()
            )
        )
    }
}
