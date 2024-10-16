package com.example.riseandroid.data.entitys

import androidx.room.Dao

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: Account)
}