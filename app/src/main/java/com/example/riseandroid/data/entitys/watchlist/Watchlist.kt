package com.example.riseandroid.data.entitys.watchlist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "watchlist"
)
data class WatchlistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int
)
