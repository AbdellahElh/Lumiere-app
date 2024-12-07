package com.example.riseandroid.ui.screens.ticket



import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.data.entitys.Tickets.TicketEntity
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.repository.ITicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface TicketUiState {
    data class Success( val ticketList: StateFlow<List<Ticket>>) : TicketUiState
    object Error : TicketUiState
    object Loading : TicketUiState
}

class TicketViewModel(
    private val ticketRepository: ITicketRepository,

    ) : ViewModel() {

    var ticketUiState: TicketUiState by mutableStateOf(TicketUiState.Loading)
        private set

    private val _allTickets = MutableStateFlow<List<Ticket>>(emptyList())
    val Alltickets = _allTickets.asStateFlow()


    init {
        getTickets()
    }
    fun getTickets() {
        viewModelScope.launch {
            try {
                val tickets = ticketRepository.getTickets()
                    .collect { tickets ->
                        _allTickets.value = tickets
                        ticketUiState  = TicketUiState.Success(
                            ticketList = Alltickets
                        )
                    }

            } catch (e: Exception) {

                _allTickets.value = emptyList()
            }
        }
    }
    fun addTicket(
        movieId: Int,
        eventId: Int,
        cinemaName: String,
        showtime: String,
        onResult: (TicketEntity?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val ticket = ticketRepository.addTicket(movieId, eventId, cinemaName, showtime)
                onResult(ticket)
            } catch (e: IOException) {
                Log.e("TicketViewModel", "Netwerk error toevoegen ticket", e)
                onResult(null)
            } catch (e: HttpException) {
                Log.e("TicketViewModel", "HTTP error toevoegen ticket", e)
                onResult(null)
            } catch (e: Exception) {
                Log.e("TicketViewModel", "Error toevoegen ticket", e)
                onResult(null)
            }
        }
    }


    companion object {



        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                val ticketRepo=application.container.ticketRepo
                TicketViewModel(ticketRepository = ticketRepo)
            }
        }
    }

}






