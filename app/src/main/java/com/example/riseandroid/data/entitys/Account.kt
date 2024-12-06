package com.example.riseandroid.data.entitys
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.model.Ticket


@Entity(tableName="Account")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val UserId : Int = 0,
    val UserName: String,
    val Password: String,
)

data class UserWithTenturncards(
    @Embedded val user : Account,
    @Relation(
        parentColumn = "UserId",
        entityColumn = "UserTenturncardId"
    )
    val tenturncards : List<Tenturncard>
)
data class UserWithTicker(
    @Embedded val user : Account,
    @Relation(
        parentColumn = "UserId",
        entityColumn = "accountId"
    )
    val tenturncards : List<Ticket>
)