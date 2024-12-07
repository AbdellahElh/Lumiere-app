package com.example.riseandroid.repository

import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.model.Ticket
import kotlinx.coroutines.flow.Flow

interface ITicketRepository {
    suspend fun getTickets(): Flow<List<Ticket>>
    suspend fun addTicket(
        movieCode: Int,
        eventCode: Int,
        cinemaName: String,
        showtime: String
    ): TicketEntity}