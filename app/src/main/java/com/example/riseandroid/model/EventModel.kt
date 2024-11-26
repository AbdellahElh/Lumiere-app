package com.example.riseandroid.model

import com.example.riseandroid.data.entitys.Cinema

data class EventModel(
    val id: Int,
    val title: String,
    val genre: String,
    val type: String,
    val description: String,
    val duration: String,
    val director: String,
    val releaseDate: String?,
    val videoPlaceholderUrl: String?,
    val cover: String?,
    val location: String?,
    val eventLink: String?,
    val cinemas: List<Cinema>
)


