package com.example.riseandroid.data.entitys.tenturncard

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TenturncardDao {


    @Query("SELECT * FROM tenturncards ORDER BY amountLeft DESC")
    fun getAllTenturncards(): Flow<List<TenturncardEntity>>


    @Query("SELECT * FROM tenturncards WHERE id = :id")
    suspend fun getTenturncardById(id: Int): TenturncardEntity?

    @Query("SELECT * FROM tenturncards WHERE ActivationCode = :activationCode")
    suspend fun getTenturncardByActivationCode(activationCode: String): TenturncardEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTenturncards(cards: List<TenturncardEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTenturncard(card : TenturncardEntity)


    @Query("UPDATE tenturncards SET amountLeft = :amountLeft  WHERE ActivationCode = :ActivationCode")
    suspend fun updateTenturncard(ActivationCode:  String , amountLeft: Int)

    @Delete
    suspend fun deleteTenturncard(card: TenturncardEntity)


    @Query("DELETE FROM tenturncards")
    suspend fun deleteAllTenturncards()
}
