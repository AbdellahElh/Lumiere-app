package com.example.riseandroid.repository

import com.example.riseandroid.model.Ticket
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface ITicketRepository {
    suspend fun getTickets(): Flow<List<Ticket>>
    fun addTicket( movieCode: Int,  eventCode: Int,  cinemaName: String,  showtime: LocalDateTime): Flow<ApiResource<Ticket>>
}