package com.example.riseandroid.data.entitys

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCinema(cinemas: CinemaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShowtimes(showtimes: List<ShowtimeEntity>)

    @Query("DELETE FROM showtimes WHERE movieId = :movieId")
    suspend fun deleteShowtimesForMovie(movieId: Int)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity>

    @Query("SELECT * FROM cinemas WHERE name = :name LIMIT 1")
    suspend fun getCinemaByName(name: String): CinemaEntity?

    @Query("""
        SELECT * FROM movies
        INNER JOIN showtimes ON movies.id = showtimes.movieId
        INNER JOIN cinemas ON showtimes.cinemaId = cinemas.id
        WHERE showtimes.showDate = :selectedDate
        AND cinemas.name IN (:selectedCinemas)
    """)
    suspend fun getFilteredMoviesByCinemaAndDate(
        selectedDate: String,
        selectedCinemas: List<String>
    ): List<MovieEntity>
}