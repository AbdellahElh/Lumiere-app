package com.example.riseandroid.util

import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.watchlist.MovieWatchlistEntity
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.MovieWatchlistModel

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
