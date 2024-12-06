package com.example.riseandroid.data.response

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.riseandroid.data.entitys.Account
import com.example.riseandroid.data.entitys.EventEntity
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.Tickets.TicketType
import java.time.LocalDateTime


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
        ForeignKey(
            entity = Account::class,
            parentColumns = ["UserId"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TicketResponse(
    val id: Int = 0,
    val dateTime: String,
    val location: String,
    val type: TicketType,
    val movieId: Int? = null,
    val eventId: Int? = null,
    val accountId: Int? = null
) {
    val price: Double
    get() = when (type) {
        TicketType.STANDAARD -> 12.0
        TicketType.SENIOR -> 11.5
        TicketType.STUDENT -> 10.0
        else -> 12.0
    }
}
