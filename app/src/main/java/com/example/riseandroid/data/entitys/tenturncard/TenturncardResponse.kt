package com.example.riseandroid.data.entitys.tenturncard

data class TenturncardResponse(
    val id: Int = 0,
    val amountLeft: Int,
    val purchaseDate: String?,
    val expirationDate: String?,
    val ActivationCode: String,
    val IsActivated: Boolean = false,

    var UserTenturncardId : Int?,
)