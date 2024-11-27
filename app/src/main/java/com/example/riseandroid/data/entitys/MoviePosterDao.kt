package com.example.riseandroid.data.entitys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviePosterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviePosters(posters: List<MoviePosterEntity>)

    @Query("SELECT * FROM movie_posters")
    fun getAllMoviePosters(): Flow<List<MoviePosterEntity>>

    @Query("DELETE FROM movie_posters")
    suspend fun deleteAllMoviePosters()

}