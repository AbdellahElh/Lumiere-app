package com.example.riseandroid.data.entitys

interface AccountRepository {
    suspend fun insertAccount(account: Account)
}