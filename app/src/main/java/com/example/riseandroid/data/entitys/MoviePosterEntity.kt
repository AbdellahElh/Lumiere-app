package com.example.riseandroid.data.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_posters")
data class MoviePosterEntity(
    @PrimaryKey val id: Int,
    val cover: String,
    val releaseDate: String
)
