package com.example.riseandroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.riseandroid.data.entitys.Account
import com.example.riseandroid.data.entitys.AccountDao
import android.content.Context

@Database(entities = [Account::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun accountDao(): AccountDao

    companion object {
        @Volatile
        private var Instance: Database? = null

        fun getDatabase(context: Context): Database {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, Database::class.java, "Database")
                    .fallbackToDestructiveMigration().build().also { Instance = it }
            }
        }
    }
}