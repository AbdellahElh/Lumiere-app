package com.example.riseandroid.fake

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.riseandroid.data.entitys.event.AddTicketDTO
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.repository.ITicketRepository
import com.example.riseandroid.ui.screens.ticket.TicketUiState
import com.example.riseandroid.ui.screens.ticket.TicketViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
class FakeTicketViewModelAll(
    private val ticketRepository: ITicketRepository
) : TicketViewModel(ticketRepository)

class FakeTicketViewModel(
    private val ticketRepository: ITicketRepository
) : TicketViewModel(ticketRepository) {

    private val _allTickets = MutableStateFlow<List<Ticket>>(emptyList())
    override val Alltickets: StateFlow<List<Ticket>> = _allTickets.asStateFlow()

    init {
        getTickets()
    }

    override fun getTickets() {
        viewModelScope.launch {
            ticketUiState = TicketUiState.Loading
            try {
                ticketRepository.getTickets()
                    .collect { tickets ->
                        _allTickets.value = tickets
                        ticketUiState = TicketUiState.Success(
                            ticketList = Alltickets
                        )
                    }
            } catch (e: Exception) {
                _allTickets.value = emptyList()
                ticketUiState = TicketUiState.Error
            }
        }
    }

    override fun addTicket(newTicket: AddTicketDTO) {
        viewModelScope.launch {
            try {
                ticketRepository.addTicket(newTicket)
            } catch (e: Exception) {
                // Handle errors, if needed
            }
        }
    }
}
