package com.example.riseandroid.data.entitys

class OfflineAccountRepository(private val accountDao: AccountDao) : AccountRepository {
    override suspend fun insertAccount(account: Account) {
        accountDao.insert(account)
    }
}