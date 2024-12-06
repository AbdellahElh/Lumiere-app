package com.example.riseandroid.model

import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.example.riseandroid.data.entitys.EventEntity
import com.example.riseandroid.data.entitys.MovieEntity




data class Ticket(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: String,
    val location: String,
    val type: Int,
    val movieId: Int? = null,
    val eventId: Int? = null,
    val accountId: Int? = null,
    @Embedded(prefix = "movie_") val movie: MovieEntity? = null,
    @Embedded(prefix = "event_") val event: EventEntity? = null
)