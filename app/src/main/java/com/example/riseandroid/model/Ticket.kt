package com.example.riseandroid.model

data class Ticket (
    val ticketId: Long,
    val userId: Long,
    val movie: String,
    val date: String,
    val hours: String,
    val location: String
)