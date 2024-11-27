// RiseDatabase.kt
package com.example.riseandroid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.riseandroid.data.entitys.*

@Database(
    entities = [
        MovieEntity::class,
        CinemaEntity::class,
        ShowtimeEntity::class,
        MoviePosterEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class RiseDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun moviePosterDao(): MoviePosterDao

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
