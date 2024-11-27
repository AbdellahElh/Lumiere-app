package com.example.riseandroid.data.entitys

import androidx.room.*

@Dao
interface TenturncardDao {


    @Query("SELECT * FROM tenturncards")
    suspend fun getAllTenturncards(): List<TenturncardEntity>


    @Query("SELECT * FROM tenturncards WHERE id = :id")
    suspend fun getTenturncardById(id: Int): TenturncardEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTenturncards(cards: List<TenturncardEntity>)

    @Insert
    suspend fun addTenturncard(card : TenturncardEntity)

    @Update
    suspend fun updateTenturncard(card: TenturncardEntity)


    @Delete
    suspend fun deleteTenturncard(card: TenturncardEntity)


    @Query("DELETE FROM tenturncards")
    suspend fun deleteAllTenturncards()
}
