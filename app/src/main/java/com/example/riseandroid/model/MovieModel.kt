package com.example.riseandroid.model

import com.example.riseandroid.data.entitys.Cinema

data class MovieModel(
    val id: Int,
    val eventId: Int?,
    val title: String,
    val genre: String,
    val description: String,
    val duration: Int,
    val director: String,
    val cast: List<String> = emptyList(),
    val releaseDate: String,
    val videoPlaceholderUrl: String?,
    val coverImageUrl: String?,
    val bannerImageUrl: String?,
    val posterImageUrl: String?,
    val movieLink: String,
    val cinemas: List<Cinema> = emptyList()
)

data class Cinema(
    val id:Int,
    val name: String,
    val showtimes: List<String> = emptyList()
)