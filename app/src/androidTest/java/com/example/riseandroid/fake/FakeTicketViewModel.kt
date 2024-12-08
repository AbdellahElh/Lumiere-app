package com.example.riseandroid.fake

import android.content.Context
import com.example.riseandroid.data.entitys.watchlist.UserManager
import com.example.riseandroid.repository.ITicketRepository
import com.example.riseandroid.repository.IWatchlistRepo
import com.example.riseandroid.ui.screens.ticket.TicketViewModel
import com.example.riseandroid.ui.screens.watchlist.WatchlistViewModel

class FakeTicketViewModel(
    ticketRepository: FakeTicketRepository
) : TicketViewModel(ticketRepository)
