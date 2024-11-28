package com.example.riseandroid.model

data class MovieModel(
    val id: Int,
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

data class MovieCinema(
    val id:Int,
    val name: String,
    val showtimes: List<String> = emptyList()
)

data class ShowtimeModel(
    val showTime: String,
    val showDate: String,
    val eventId: Int? = null
)