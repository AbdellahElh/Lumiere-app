package com.example.riseandroid.data.lumiere




import com.example.riseandroid.data.Datasource
import com.example.riseandroid.model.Program
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.network.LumiereApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

interface TicketRepository {
    suspend fun getTicketsPerUser(userId: Long) : Flow<List<Ticket>>
    suspend fun MakeTicketsPerUser(userId: Long) :Unit

}

class NetworkTicketRepository(private val lumiereApiService: LumiereApiService) : TicketRepository {
    override suspend fun getTicketsPerUser(userId: Long): Flow<List<Ticket>> {
        val tickets = Datasource().loadTickets()
            .filter { it.userId == userId }
            .sortedByDescending { it.date }
        return listOf(tickets).asFlow()
    }
    override suspend fun MakeTicketsPerUser(userId: Long): Unit {
        //moet nog gemaakt worden
        return
    }


}


