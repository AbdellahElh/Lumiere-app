package com.example.riseandroid.util

import com.example.riseandroid.data.entitys.Cinema
import com.example.riseandroid.data.entitys.EventEntity
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.watchlist.MovieWatchlistEntity
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MovieWatchlistModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val gson = Gson()

fun MovieEntity.asExternalModel(): MovieModel {
    return MovieModel(
        id = id,
        title = title,
        coverImageUrl = coverImageUrl,
        genre = genre,
        duration = duration,
        director = director,
        description = description,
        movieLink = movieLink,
        videoPlaceholderUrl = videoPlaceholderUrl,
        cast = emptyList(),
        cinemas = emptyList(),
        releaseDate = releaseDate,
        bannerImageUrl = bannerImageUrl,
        posterImageUrl = posterImageUrl,
    )
}

fun MovieModel.asEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        genre = genre ?: "",
        description = description ?: "",
        duration = duration ?: 0,
        director = director ?: "",
        videoPlaceholderUrl = videoPlaceholderUrl,
        coverImageUrl = coverImageUrl,
        bannerImageUrl = coverImageUrl,
        posterImageUrl = coverImageUrl,
        movieLink = movieLink,
        releaseDate = releaseDate,
    )
}

fun MovieWatchlistEntity.asExternalModel(): MovieWatchlistModel {
    return MovieWatchlistModel(
        watchlistId = this.watchlistId,
        movieId = this.movieId
    )
}

fun MovieWatchlistModel.asEntity(): MovieWatchlistEntity {
    return MovieWatchlistEntity(
        watchlistId = this.watchlistId,
        movieId = this.movieId
    )
}


fun EventEntity.asExternalModel(): EventModel {
    val cinemaType = object : TypeToken<List<Cinema>>() {}.type
    val cinemaList: List<Cinema> = gson.fromJson(cinemasJson, cinemaType) ?: emptyList()

    return EventModel(
        id = id,
        title = title,
        cover = cover,
        genre = genre,
        type = type,
        duration = "$duration minuten",
        director = director,
        description = description,
        eventLink = eventLink,
        videoPlaceholderUrl = videoPlaceholderUrl,
        releaseDate = releaseDate,
        cinemas = cinemaList,
        location = location ?: ""
    )
}

fun EventModel.asEntity(): EventEntity {
    val durationInMinutes = duration?.let {
        // Extract numeric value from the string
        val regex = Regex("\\d+")
        val matchResult = regex.find(it)
        matchResult?.value?.toIntOrNull() ?: 0
    } ?: 0

    // Convert List<Cinema> to JSON string
    val cinemasJson = gson.toJson(cinemas)

    return EventEntity(
        id = id,
        title = title ?: "",
        genre = genre ?: "",
        type = type ?: "",
        description = description ?: "",
        duration = durationInMinutes,
        director = director ?: "",
        releaseDate = releaseDate ?: "",
        videoPlaceholderUrl = videoPlaceholderUrl,
        cover = cover,
        eventLink = eventLink ?: "",
        cinemasJson = cinemasJson,
        location = location ?: ""
    )
}