package com.example.riseandroid.data.entitys
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val genre: String,
    val description: String,
    val duration: String,
    val director: String,
    val releaseDate: Long,
    val videoPlaceholderUrl: String?,
    val coverImageUrl: String?,
    val bannerImageUrl: String?,
    val posterImageUrl: String?,
    val movieLink: String
)

@Entity(tableName = "cinemas")
data class CinemaEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val location: String?
)

@Entity(
    tableName = "showtimes",
    foreignKeys = [
        ForeignKey(entity = MovieEntity::class, parentColumns = ["id"], childColumns = ["movieId"]),
        ForeignKey(entity = CinemaEntity::class, parentColumns = ["id"], childColumns = ["cinemaId"])
    ],
    indices = [Index("movieId"), Index("cinemaId")]
)
data class ShowtimeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cinemaId: Int,
    val movieId: Int,
    val showTime: String,
    val showDate:String
)