package com.example.riseandroid.ui.screens.account

import androidx.lifecycle.ViewModel
import com.example.riseandroid.repository.IAuthRepo
import com.example.riseandroid.ui.screens.signUp.AuthViewModel

class AccountViewModel(private val authRepo: IAuthRepo, private val authViewModel: AuthViewModel) : ViewModel() {

//    private val _isLoggedOut = mutableStateOf(false)
//    val isLoggedOut: State<Boolean> get() = _isLoggedOut
//
//    fun logout() {
//        viewModelScope.launch {
//            authRepo.logout()
//            _isLoggedOut.value = true
//        }
//    }
}