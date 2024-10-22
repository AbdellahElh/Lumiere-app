package com.example.riseandroid.ui.screens.Checkout

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
import com.example.riseandroid.data.lumiere.TicketRepository
import com.example.riseandroid.model.Movie
import com.example.riseandroid.model.Ticket
import com.example.riseandroid.ui.screens.homepage.HomepageViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface CheckoutUiState {
    data class Success(val ticketTypes : Flow<List<Ticket>>) : CheckoutUiState
    object Error : CheckoutUiState
    object Loading : CheckoutUiState
}

class CheckoutViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    var checkoutUiState: CheckoutUiState by mutableStateOf(CheckoutUiState.Loading)
        private set

    init {
        getTicketTypes()
    }

    private fun getTicketTypes() {
        viewModelScope.launch {
            checkoutUiState = CheckoutUiState.Loading
            checkoutUiState = try {
                val tickets = ticketRepository.getTicketTypes()
                CheckoutUiState.Success(tickets)
            } catch (e: IOException) {
                CheckoutUiState.Error
            } catch (e: HttpException) {
                CheckoutUiState.Error
            }
        }
    }
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                val ticketRepository = application.container.ticketRepository
                CheckoutViewModel(ticketRepository)
            }
        }
    }
}
