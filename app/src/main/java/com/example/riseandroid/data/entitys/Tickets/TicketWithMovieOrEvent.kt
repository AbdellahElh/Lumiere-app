//package com.example.riseandroid.data.entitys.Tickets
//
//import androidx.room.Embedded
//import androidx.room.Entity
//import androidx.room.ForeignKey
//import androidx.room.Relation
//import com.example.riseandroid.data.entitys.EventEntity
//import com.example.riseandroid.data.entitys.MovieEntity
//@Entity(
//    tableName = "ticketMovieOrEvent"
//
//)data class TicketWithMovieOrEvent(
//    @Embedded val ticket: TicketEntity,
//    @Relation(parentColumn = "movieId", entityColumn = "id", entity = MovieEntity::class)
//    val movie: MovieEntity? = null,
//    @Relation(parentColumn = "eventId", entityColumn = "id", entity = EventEntity::class)
//    val event: EventEntity? = null
//)