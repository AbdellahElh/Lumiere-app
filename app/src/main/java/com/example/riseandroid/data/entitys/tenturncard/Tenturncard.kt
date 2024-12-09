package com.example.riseandroid.data.entitys.tenturncard

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tenturncards")
data class TenturncardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amountLeft: Int,
    val purchaseDate: String?,
    val expirationDate: String?,
    val ActivationCode: String,
    val IsActivated: Boolean = false,

    var UserTenturncardId : Int? = 1,
)
