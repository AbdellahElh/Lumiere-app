package com.example.riseandroid.fake

import com.example.riseandroid.data.entitys.watchlist.UserManager
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserManager : UserManager() {
     override val currentUserId = MutableStateFlow<Int?>(1)
}

