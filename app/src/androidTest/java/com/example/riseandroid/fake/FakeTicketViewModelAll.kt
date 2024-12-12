package com.example.riseandroid.fake

import com.example.riseandroid.repository.ITicketRepository
import com.example.riseandroid.ui.screens.ticket.TicketViewModel

class FakeTicketViewModelAll(
    private val ticketRepository: ITicketRepository
) : TicketViewModel(ticketRepository)