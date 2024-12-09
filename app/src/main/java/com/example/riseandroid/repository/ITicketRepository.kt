package com.example.riseandroid.repository

import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.data.entitys.event.AddTicketDTO
import com.example.riseandroid.model.Ticket
import kotlinx.coroutines.flow.Flow

interface ITicketRepository {
    suspend fun getTickets(): Flow<List<Ticket>>
    suspend fun addTicket( ticket: AddTicketDTO): TicketEntity

}