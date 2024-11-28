package com.example.riseandroid.data.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "tenturncards")
data class TenturncardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amountLeft: Int,
    val purchaseDate: String?,
    val expirationDate: String?,
    val ActivationCode: String,
    val IsActivated: Boolean = false,
    //define a one-to-many relation with user - tenturncard
    val UserTenturncardId : Int? = 1,
)
