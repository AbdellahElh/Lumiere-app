package com.example.riseandroid.data.lumiere


import com.example.riseandroid.data.Datasource
import com.example.riseandroid.model.Movie
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.network.LumiereApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface TicketRepository {
    suspend fun getTicketTypes() : Flow<List<Ticket>>



}

class NetworkTicketRepository(private val lumiereApiService: LumiereApiService) : TicketRepository {
    override suspend fun getTicketTypes(): Flow<List<Ticket>> {
        val ticket = Datasource().ticketTypes()
        return listOf(ticket).asFlow()
    }



}