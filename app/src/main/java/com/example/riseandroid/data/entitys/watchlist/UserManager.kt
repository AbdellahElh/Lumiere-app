package com.example.riseandroid.data.entitys.watchlist

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class UserManager {
    private val _currentUserId = MutableStateFlow<Int?>(null)
    open val currentUserId: StateFlow<Int?> = _currentUserId.asStateFlow()

    fun setUserId(userId: Int) {
        _currentUserId.value = userId
    }

    fun clearUserId() {
        _currentUserId.value = null
    }
}
