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
import com.example.riseandroid.data.entitys.Tickets.TicketDao
import com.example.riseandroid.data.entitys.Tickets.TicketEntity

@Database(
    entities = [
        MovieEntity::class,
        CinemaEntity::class,
        ShowtimeEntity::class,
        MoviePosterEntity::class,
        TenturncardEntity::class,
        TicketEntity::class,
        EventEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class RiseDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
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
