package com.example.riseandroid.data.response

import com.example.riseandroid.data.entitys.Cinema
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.model.Movie

data class EventResponse(
    val id: Int,
    val title: String?,
    val genre: String?,
    val type: String?,
    val description: String?,
    val duration: String?,
    val director: String?,
    val releaseDate: String?,
    val videoPlaceholderUrl: String?,
    val cover: String?,
    val location: String?,
    val eventLink: String?,
    val cinemas: List<Cinema>?,
    val movies: List<MovieEntity>?
)
