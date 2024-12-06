package com.example.riseandroid.data.entitys.Tickets

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.riseandroid.data.entitys.Account
import com.example.riseandroid.data.entitys.EventEntity
import com.example.riseandroid.data.entitys.MovieEntity
import java.time.LocalDateTime

enum class TicketType {
    STANDAARD,
    SENIOR,
    STUDENT
}

@Entity(
    tableName = "ticket",
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),

    ]
)
data class TicketEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: String,
    val location: String,
    val type: TicketType,
    val movieId: Int? = null,
    val eventId: Int? = null,
    val accountId: Int? = null,

    @Embedded(prefix = "movie_") val movie: MovieEntity? = null,
    @Embedded(prefix = "event_") val event: EventEntity? = null
) {
    val price: Double
        get() = when (type) {
            TicketType.STANDAARD -> 12.0
            TicketType.SENIOR -> 11.5
            TicketType.STUDENT -> 10.0
            else -> 12.0
        }
}