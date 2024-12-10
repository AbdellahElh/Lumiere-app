package com.example.riseandroid.network

import androidx.room.Query
import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.data.entitys.event.AddTicketDTO
import com.example.riseandroid.data.response.TicketResponse
import com.example.riseandroid.model.Ticket
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.LocalDateTime

interface TicketApi {

    @GET("/api/ticket")
    suspend fun getTickets(): List<TicketResponse>

    @POST("/api/ticket/add/")
    suspend fun addTicket(
        @Body ticket: AddTicketDTO
    )



}
