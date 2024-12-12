package com.example.riseandroid.util

import com.example.riseandroid.data.entitys.Cinema
import com.example.riseandroid.data.entitys.EventEntity
import com.example.riseandroid.data.entitys.tenturncard.TenturncardEntity
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.watchlist.MovieWatchlistEntity
import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.data.entitys.tenturncard.TenturncardResponse

import com.example.riseandroid.data.response.EventResponse
import com.example.riseandroid.data.response.TicketResponse
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.model.MovieWatchlistModel
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.model.Ticket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val gson = Gson()

fun TenturncardEntity.asExternalModel(): Tenturncard {
    return Tenturncard(
        id = id,
        amountLeft = amountLeft,
        purchaseDate = purchaseDate ?: "",
        expirationDate = expirationDate ?: "",
        IsActivated = IsActivated ?: false,
        ActivationCode = ActivationCode ?: "",
    )
}
fun TenturncardResponse.asEntity(): TenturncardEntity {
    return TenturncardEntity(
        id = id,
        amountLeft = amountLeft,
        purchaseDate = purchaseDate,
        expirationDate = expirationDate,
        IsActivated = isActivated?: false,
        ActivationCode = activationCode?: "",

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
fun TenturncardResponse.asDomainModel(): Tenturncard {
    return Tenturncard(
        id = id,
        amountLeft = amountLeft,
        purchaseDate = purchaseDate?:"",
        expirationDate = expirationDate?:"",
        IsActivated = isActivated?: false,
        ActivationCode = activationCode?: "",

        )
}

fun TicketEntity.asExternalModel(): Ticket {
    return Ticket(
        id = id,
        dateTime = dateTime,
        location = location,
        type = type,
        movieId = movieId,
        eventId = eventId,
        accountId = accountId,
        movie = movie,
        event = event

    )
}

fun Ticket.asEntity(): TicketEntity {
    return TicketEntity(
        id = id,
        dateTime = dateTime ?: "unknown",
        location = location ?: "unknown",
        type = type ?: 0,
        movieId = movieId,
        eventId = eventId,
        accountId = accountId,
        movie = movie,
        event = event
    )
}
fun TicketResponse.asExternalModel(): Ticket {
    return Ticket(
        id = id,
        dateTime = dateTime,
        location = location,
        type = type,
        movieId = movieId,
        eventId = eventId,
        accountId = accountId,
        movie = movie,
        event = event

    )
}

fun TicketResponse.asEntity(): TicketEntity {
    return TicketEntity(
        id = id,
        dateTime = dateTime ?: "unknown",
        location = location ?: "unknown",
        type = type ?: 0,
        movieId = movieId,
        eventId = eventId,
        accountId = accountId,
        movie = movie,
        event = event
    )
}
fun EventEntity.asExternalModel(): EventModel {
    val cinemaType = object : TypeToken<List<Cinema>>() {}.type
    val movieType = object : TypeToken<List<MovieEntity>>() {}.type

    val cinemaList: List<Cinema> = gson.fromJson(cinemasJson, cinemaType) ?: emptyList()
    val movieList: List<MovieEntity> = gson.fromJson(moviesJson,movieType ) ?: emptyList()

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
        location = location ?: "",
        movies = movieList
    )
}

fun EventModel.asEntity(): EventEntity {
    val durationInMinutes = duration?.let {
        // Extract numeric value from the string
        val regex = Regex("\\d+")
        val matchResult = regex.find(it)
        matchResult?.value?.toIntOrNull() ?: 0
    } ?: 0

    val cinemasJson = gson.toJson(cinemas)
    val movieJson = gson.toJson(movies)


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
        location = location ?: "",
        moviesJson = movieJson
    )
}

fun EventResponse.toDomainModel(): EventModel {
    return EventModel(
        id = id,
        title = title ?: "Geen titel",
        genre = genre ?: "Onbekend genre",
        type = type ?: "Onbekend type",
        description = description ?: "Geen beschrijving",
        duration = duration ?: "0 minuten",
        director = director ?: "Onbekend",
        releaseDate = releaseDate,
        videoPlaceholderUrl = videoPlaceholderUrl,
        cover = cover,
        location = location ?: "Onbekend",
        eventLink = eventLink ?: "",
        cinemas = cinemas ?: emptyList(),
        movies = movies ?: emptyList()
    )
}

fun EventResponse.toEntity(): EventEntity {
    val cinemasJson = gson.toJson(cinemas)
    val movieJson = gson.toJson(movies)

    val durationInMinutes = duration?.let {
        val regex = Regex("\\d+")
        regex.find(it)?.value?.toIntOrNull() ?: 0
    } ?: 0

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
        location = location ?: "",
        moviesJson = movieJson
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
