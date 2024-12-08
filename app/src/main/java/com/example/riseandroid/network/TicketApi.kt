package com.example.riseandroid.network

import androidx.room.Query
import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.data.response.TicketResponse
import com.example.riseandroid.model.Ticket
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.LocalDateTime

interface TicketApi {

    @GET("/api/ticket")
    suspend fun getTickets(): List<TicketResponse>

    @POST("/api/ticket/add/")
    fun addTicket(
        @retrofit2.http.Query("MovieId") movieId: Int,
        @retrofit2.http.Query("EventId") eventId: Int,
        @retrofit2.http.Query("cinemaName") cinemaName: String,
        @retrofit2.http.Query("showtime") showtime: String
    ): Call<TicketResponse>


}
