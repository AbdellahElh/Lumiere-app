package com.example.riseandroid.network

import androidx.room.Query
import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.model.Ticket
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.LocalDateTime

interface TicketApi {

    @GET("/api/ticket")
    suspend fun getTickets(): List<Ticket>

//    @POST("/api/ticket/add/")
//    fun addTicket(
//        @Query("MovieId") movieId: Int,
//        @Query("EventId") eventId: Int,
//        @Query("cinemaName") cinemaName: String,
//        @Query("showtime") showtime: LocalDateTime
//    ): Call<Unit>
}
