package com.example.riseandroid.network


import com.example.riseandroid.model.EventModel
import retrofit2.http.GET
import retrofit2.http.Query

interface EventsApi {
    @GET("api/Event")
    suspend fun getAllEvents(
        @Query("cinema") cinemas: List<String> = listOf("Brugge", "Antwerpen", "Mechelen", "Cinema Cartoons")
    ): List<EventModel>
}

