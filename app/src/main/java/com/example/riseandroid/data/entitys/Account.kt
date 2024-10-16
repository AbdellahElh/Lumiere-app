package com.example.riseandroid.data.entitys
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="Account")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val UserId : Int = 0,

    val UserName: String,
    val Password: String,
) {
}