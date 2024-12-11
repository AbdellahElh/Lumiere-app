package com.example.riseandroid.data.entitys.tenturncard

data class TenturncardResponse(
    val id: Int = 0,
    var accountId : Int?,
    var activationCode: String,
    var isActivated: Boolean = false,
    var amountLeft: Int,
    var purchaseDate: String? = null,
    var expirationDate: String? = null,
) {
    init {
        if (purchaseDate.isNullOrEmpty()) {
            this.purchaseDate = null
        }
        if (expirationDate.isNullOrEmpty()) {
            this.expirationDate = null
        }
    }
}
