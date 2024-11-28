package com.example.riseandroid.data.entitys.watchlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.riseandroid.data.entitys.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    @Query("""
    SELECT movies.* 
    FROM movies
    INNER JOIN moviewatchlist ON movies.id = moviewatchlist.movieId
    INNER JOIN watchlist ON moviewatchlist.watchlistId = watchlist.id
    WHERE watchlist.userId = :userId
""")
    fun getMoviesInWatchlist(userId: Int): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(movieWatchlistEntity: MovieWatchlistEntity)

    @Query("""
        DELETE FROM moviewatchlist 
        WHERE movieId = :movieId AND watchlistId = (
            SELECT id FROM watchlist WHERE userId = :userId
        )
    """)
    suspend fun removeFromWatchlist(movieId: Int, userId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToWatchlist(movieEntity: MovieEntity)

    @Query("SELECT id FROM watchlist WHERE userId = :userId LIMIT 1")
    suspend fun getWatchlistIdForUser(userId: Int): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(watchlistEntity: WatchlistEntity)

    @Query("DELETE FROM moviewatchlist WHERE watchlistId = (SELECT id FROM watchlist WHERE userId = :userId)")
    suspend fun clearWatchlistForUser(userId: Int)

    @Query("DELETE FROM moviewatchlist")
    suspend fun clearAllMovieWatchlistEntries()

    @Query("DELETE FROM watchlist")
    suspend fun clearAllWatchlists()

    @Transaction
    suspend fun clearAllWatchlistData() {
        clearAllMovieWatchlistEntries()
        clearAllWatchlists()
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieWatchlistRelations(relations: List<MovieWatchlistEntity>)


}
