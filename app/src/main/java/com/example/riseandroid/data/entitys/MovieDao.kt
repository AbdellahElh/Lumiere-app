package com.example.riseandroid.data.entitys

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCinema(cinemas: CinemaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShowtimes(showtimes: List<ShowtimeEntity>)

    @Query("DELETE FROM showtimes WHERE movieId = :movieId")
    suspend fun deleteShowtimesForMovie(movieId: Int)

    @Query("""SELECT DISTINCT movies.id, movies.title,movies.coverImageUrl,genre,description,duration,director,movieLink,cinemaId
        FROM movies
        INNER JOIN showtimes ON movies.id = showtimes.movieId
        INNER JOIN cinemas ON showtimes.cinemaId = cinemas.id """)
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM cinemas WHERE name = :name LIMIT 1")
    suspend fun getCinemaByName(name: String): CinemaEntity?

    @Query("""
        SELECT DISTINCT movies.id, movies.title,movies.coverImageUrl,genre,description,duration,director,movieLink
        FROM movies
        INNER JOIN showtimes ON movies.id = showtimes.movieId
        INNER JOIN cinemas ON showtimes.cinemaId = cinemas.id
        WHERE showtimes.showDate = :selectedDate
        AND cinemas.name IN (:selectedCinemas)
    """)
    fun getFilteredMoviesByCinemaAndDate(
        selectedDate: String,
        selectedCinemas: List<String>
    ): Flow<List<MovieEntity>>
}