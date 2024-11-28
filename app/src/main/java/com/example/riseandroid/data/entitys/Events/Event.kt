package com.example.riseandroid.data.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val genre: String,
    val type: String,
    val description: String,
    val duration: Int,
    val director: String,
    val releaseDate: String?,
    val videoPlaceholderUrl: String?,
    val cover: String?,
    val location: String?,
    val eventLink: String,
    val cinemasJson: String
)

data class Cinema(
    val id: Int,
    val name: String,
    val showtimes: List<String>
)