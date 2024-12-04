package com.example.riseandroid.ui.screens.eventDetail

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
import com.example.riseandroid.model.EventModel
import com.example.riseandroid.repository.EventRepo
import kotlinx.coroutines.launch

sealed interface EventDetailUiState {
    data class Success(val specificEvent: EventModel) : EventDetailUiState
    object Error : EventDetailUiState
    object Loading : EventDetailUiState
}

class EventDetailViewModel(
    private val eventId: Int,
    private val eventRepo: EventRepo
) : ViewModel() {

    var eventDetailUiState: EventDetailUiState by mutableStateOf(EventDetailUiState.Loading)
        private set

    init {
        getEvent()
    }

    private fun getEvent() {
        viewModelScope.launch {
            eventDetailUiState = EventDetailUiState.Loading
            eventDetailUiState = try {
                val specificEvent = eventRepo.getSpecificEvent(eventId)
                if (specificEvent != null) {
                    EventDetailUiState.Success(specificEvent)
                } else {
                    EventDetailUiState.Error
                }
            } catch (e: Exception) {
                EventDetailUiState.Error
            }
        }
    }

    companion object {
        fun provideFactory(eventId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                val eventsRepository = application.container.eventRepo
                EventDetailViewModel(eventId, eventsRepository)
            }
        }
    }
}
