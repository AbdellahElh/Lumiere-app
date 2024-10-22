package com.example.riseandroid.model

data class CardInfo(
    val name: String,
    val cardNumber: String,
    val expiryDate: String,
    val cvv: String
) {
    val last4Digits: String get() = cardNumber.takeLast(4)
}