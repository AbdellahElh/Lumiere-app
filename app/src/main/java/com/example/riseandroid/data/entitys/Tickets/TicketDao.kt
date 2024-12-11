package com.example.riseandroid.data.entitys.Tickets

import androidx.room.*
import com.example.riseandroid.data.entitys.EventEntity
import com.example.riseandroid.data.entitys.MovieEntity
import com.example.riseandroid.data.response.EventResponse
import com.example.riseandroid.network.ResponseMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {


    @Query("SELECT * FROM ticket")
    fun getTickets(): Flow<List<TicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTickets(tickets: List<TicketEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTicket(ticket: TicketEntity)


    @Delete
    suspend fun deleteTicket(ticket: TicketEntity)


    @Query("DELETE FROM ticket")
    suspend fun deleteAllTickets()
}
