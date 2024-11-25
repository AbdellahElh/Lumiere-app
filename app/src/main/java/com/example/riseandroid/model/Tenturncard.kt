package com.example.riseandroid.model

data class Tenturncard(
    val id: Int,
    val amountLeft: Int,
    val purchaseDate: String?,
    val expirationDate: String?,
    val IsActivated: Boolean,
    val ActivationCode: String,
    ) {
}
