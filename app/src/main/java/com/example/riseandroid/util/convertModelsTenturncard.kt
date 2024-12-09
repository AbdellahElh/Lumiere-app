package com.example.riseandroid.util

import com.example.riseandroid.data.entitys.tenturncard.TenturncardEntity
import com.example.riseandroid.data.entitys.tenturncard.TenturncardResponse
import com.example.riseandroid.model.Tenturncard

fun TenturncardEntity.asResponse() : TenturncardResponse {
    return TenturncardResponse(
        id = id,
        amountLeft = amountLeft,
        purchaseDate = purchaseDate,
        expirationDate = expirationDate,
        ActivationCode = ActivationCode,
        IsActivated = IsActivated,
        UserTenturncardId = UserTenturncardId,
    )
}

fun Tenturncard.asResponse() : TenturncardResponse {
    return TenturncardResponse(
        id = id,
        amountLeft = amountLeft,
        purchaseDate = purchaseDate,
        expirationDate = expirationDate,
        ActivationCode = ActivationCode,
        IsActivated = IsActivated,
        UserTenturncardId = null
    )
}