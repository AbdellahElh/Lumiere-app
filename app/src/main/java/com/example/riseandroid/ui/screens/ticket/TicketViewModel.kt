package com.example.riseandroid.ui.screens.ticket



import android.app.Application
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
import com.example.riseandroid.data.lumiere.MoviesRepository
import com.example.riseandroid.data.lumiere.ProgramRepository
import com.example.riseandroid.data.lumiere.TicketRepository
import com.example.riseandroid.model.Movie
import com.example.riseandroid.model.Program
import com.example.riseandroid.model.Ticket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface TicketUiState {
    data class Success( val ticketList: Flow<List<Ticket>>) : TicketUiState
    object Error : TicketUiState
    object Loading : TicketUiState
}

class TicketViewModel(
    private val userId: Long,
    private val ticketRepository: TicketRepository,

    ) : ViewModel() {

    var ticketUiState: TicketUiState by mutableStateOf(TicketUiState.Loading)
        private set

    private val _allTickets = MutableStateFlow<List<Ticket>>(emptyList())
    val allTickets: StateFlow<List<Ticket>> = _allTickets.asStateFlow()

    init {
        getTickets()
    }

    private fun getTickets() {
        viewModelScope.launch {
            ticketUiState = TicketUiState.Loading
            ticketUiState = try {
                val ticketList = ticketRepository.getTicketsPerUser(userId)

                TicketUiState.Success(ticketList)
            } catch (e: IOException) {
                TicketUiState.Error
            } catch (e: HttpException) {
                TicketUiState.Error
            }
        }
    }

    companion object {
        fun provideFactory(userId: Long): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                val ticketRepository = application.container.ticketRepository

                TicketViewModel(userId, ticketRepository )
            }
        }
    }
}






