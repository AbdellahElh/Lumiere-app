// RiseDatabase.kt
package com.example.riseandroid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.riseandroid.data.entitys.CinemaEntity
import com.example.riseandroid.data.entitys.EventDao
import com.example.riseandroid.data.entitys.EventEntity
import com.example.riseandroid.data.entitys.MovieDao
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.entitys.MoviePosterDao
import com.example.riseandroid.data.entitys.MoviePosterEntity
import com.example.riseandroid.data.entitys.ShowtimeEntity
import com.example.riseandroid.data.entitys.TenturncardDao
import com.example.riseandroid.data.entitys.TenturncardEntity
import com.example.riseandroid.data.entitys.watchlist.MovieWatchlistEntity
import com.example.riseandroid.data.entitys.watchlist.WatchlistDao
import com.example.riseandroid.data.entitys.watchlist.WatchlistEntity
import com.example.riseandroid.data.entitys.Tickets.TicketDao
import com.example.riseandroid.data.entitys.Tickets.TicketEntity

@Database(entities = [MovieEntity::class, CinemaEntity::class, ShowtimeEntity::class,TicketEntity::class, WatchlistEntity::class, MovieWatchlistEntity::class, MoviePosterEntity::class, TenturncardEntity::class, EventEntity::class], version = 14 , exportSchema = false)
abstract class RiseDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun watchlistDao(): WatchlistDao
    abstract fun ticketDao(): TicketDao
    abstract fun moviePosterDao(): MoviePosterDao
    abstract fun tenturncardDao() : TenturncardDao
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var instance: RiseDatabase? = null

        fun getDatabase(context: Context): RiseDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    RiseDatabase::class.java,
                    "rise_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
