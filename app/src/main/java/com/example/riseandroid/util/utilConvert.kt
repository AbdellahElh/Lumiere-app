package com.example.riseandroid.util

import com.example.riseandroid.data.entitys.EventEntity
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.model.MovieModel

fun MovieEntity.asExternalModel(): MovieModel {
    return MovieModel(
        id = id,
        title = title,
        coverImageUrl = coverImageUrl,
        genre = genre,
        duration = duration,
        director = director,
        description = description,
        video = movieLink,
        videoPlaceholderUrl = videoPlaceholderUrl,
        cast = emptyList(),
        cinemas = emptyList()
    )
}

fun MovieModel.asEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        genre = genre ?: "",
        description = description ?: "",
        duration = duration ?: "",
        director = director ?: "",
        videoPlaceholderUrl = videoPlaceholderUrl,
        coverImageUrl = coverImageUrl,
        bannerImageUrl= coverImageUrl,
        posterImageUrl = coverImageUrl,
        movieLink = video ?: ""
    )
}

fun EventEntity.asExternalModel(): EventModel {
    return EventModel(
        id = id,
        title = title,
        cover = cover,
        genre = genre,
        type = type,
        duration = "$duration minuten",
        director = director,
        description = description,
        price = price,
        eventLink = eventLink,
        videoPlaceholderUrl = videoPlaceholderUrl,
        releaseDate = releaseDate,
        date = date,
        location = location
    )
}

fun EventModel.asEntity(): EventEntity {
    val durationInMinutes = duration?.let {
        // Extract numeric value from the string
        val regex = Regex("\\d+")
        val matchResult = regex.find(it)
        matchResult?.value?.toIntOrNull() ?: 0
    } ?: 0

    return EventEntity(
        id = id,
        title = title ?: "",
        genre = genre ?: "",
        type = type ?: "",
        description = description ?: "",
        price = price ?: "",
        duration = durationInMinutes,
        director = director ?: "",
        releaseDate = releaseDate ?: "",
        videoPlaceholderUrl = videoPlaceholderUrl,
        cover = cover,
        eventLink = eventLink ?: "",
        date = date,
        location = location
    )
}