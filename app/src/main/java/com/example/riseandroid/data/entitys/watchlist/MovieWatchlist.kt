package com.example.riseandroid.data.entitys.watchlist

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.riseandroid.data.entitys.MovieEntity

@Entity(
    tableName = "moviewatchlist",
    primaryKeys = ["watchlistId", "movieId"],
    foreignKeys = [
        ForeignKey(entity = MovieEntity::class, parentColumns = ["id"], childColumns = ["movieId"]),
        ForeignKey(entity = WatchlistEntity::class, parentColumns = ["id"], childColumns = ["watchlistId"])
    ]
)
data class MovieWatchlistEntity(
    val watchlistId: Int,
    val movieId: Int
)

