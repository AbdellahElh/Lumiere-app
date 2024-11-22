package com.example.riseandroid.model

data class MovieModel(
    val id: Int,
    val title: String,
    val coverImageUrl: String?,
    val genre: String?,
    val duration: String?,
    val director: String?,
    val description: String?,
    val video: String?,
    val videoPlaceholderUrl: String?,
    val cast: List<String> = emptyList(),
    val cinemas: List<Cinema> = emptyList()
)

data class Cinema(
    val id:Int,
    val name: String,
    val showtimes: List<String> = emptyList()
)