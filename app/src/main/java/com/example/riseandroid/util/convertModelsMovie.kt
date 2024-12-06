package com.example.riseandroid.util

import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.MoviePosterEntity
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.network.ResponseMovie

fun MovieEntity.asExternalModel(): MovieModel {
    return MovieModel(
        id = id,
        eventId = eventId ?: 0,
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

fun MovieEntity.asDomainModel(): MovieModel {
    return MovieModel(
        id = id,
        eventId = eventId ?: 0,
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
        posterImageUrl = posterImageUrl
    )
}

fun MovieEntity.asResponse(): ResponseMovie {
    return ResponseMovie(
        id = id,
        eventId = eventId ?: 0,
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
        posterImageUrl = posterImageUrl
    )
}

fun MovieModel.asEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        eventId = eventId,
        title = title,
        coverImageUrl = coverImageUrl,
        genre = genre,
        duration = duration,
        director = director,
        description = description,
        movieLink = movieLink,
        videoPlaceholderUrl = videoPlaceholderUrl,
        releaseDate = releaseDate,
        bannerImageUrl = bannerImageUrl,
        posterImageUrl = posterImageUrl,
    )
}

fun ResponseMovie.asDomainModel(): MovieModel {
    return MovieModel(
        id = id,
        eventId = eventId ?: 0,
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

fun ResponseMovie.asEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        eventId = eventId ?: 0,
        title = title,
        coverImageUrl = coverImageUrl,
        genre = genre,
        duration = duration,
        director = director,
        description = description,
        movieLink = movieLink,
        videoPlaceholderUrl = videoPlaceholderUrl,
        releaseDate = releaseDate,
        bannerImageUrl = bannerImageUrl,
        posterImageUrl = posterImageUrl,
    )
}

fun MovieModel.asResponseModel(): ResponseMovie {
    return ResponseMovie(
        id = id,
        eventId = eventId,
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

fun MoviePosterEntity.asExternalModel(): MoviePoster {
    return MoviePoster(
        id = id,
        cover = cover,
        releaseDate = releaseDate
    )
}

fun MoviePoster.asEntity(): MoviePosterEntity {
    return MoviePosterEntity(
        id = id,
        cover = cover,
        releaseDate = releaseDate
    )
}

