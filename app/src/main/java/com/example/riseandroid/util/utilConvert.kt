package com.example.riseandroid.util

import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.TenturncardEntity
import com.example.riseandroid.data.entitys.MoviePosterEntity
import com.example.riseandroid.model.MoviePoster
import com.example.riseandroid.model.MovieModel
import com.example.riseandroid.model.Tenturncard

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

fun TenturncardEntity.asExternalModel(): Tenturncard {
    return Tenturncard(
        id = id,
        amountLeft = amountLeft,
        purchaseDate = purchaseDate?: "",
        expirationDate = expirationDate?:"",
        IsActivated = IsActivated?: false,
        ActivationCode = ActivationCode?: "",
    )
}
fun Tenturncard.asEntity(): TenturncardEntity {
    return TenturncardEntity(
        id = id,
        amountLeft = amountLeft,
        purchaseDate = purchaseDate,
        expirationDate = expirationDate,
        IsActivated = IsActivated?: false,
        ActivationCode = ActivationCode?: "",

    )
}