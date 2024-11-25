package com.example.riseandroid.model

data class EventModel(
    val id: Int,
    val title: String,
    val genre: String,
    val type: String,
    val description: String,
    val price: String,
    val duration: String,
    val director: String,
    val releaseDate: String,
    val videoPlaceholderUrl: String?,
    val cover: String?,
    val date: String?,
    val location: String?,
    val eventLink: String
)

