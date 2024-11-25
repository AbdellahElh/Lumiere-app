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
    val price: String,
    val duration: Int,
    val director: String,
    val releaseDate: String,
    val videoPlaceholderUrl: String?,
    val cover: String?,
    val eventLink: String,
    val date: String?,
    val location: String?
)
