package com.example.riseandroid.data.entitys.Tickets

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {


    @Query("SELECT * FROM ticket")
    fun getTickets(): Flow<List<TicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTickets(tickets: List<TicketEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTicket(ticket: TicketEntity)


    @Delete
    suspend fun deleteTicket(ticket: TicketEntity)


    @Query("DELETE FROM ticket")
    suspend fun deleteAllTickets()
}
