package com.example.riseandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.repository.TenturncardRepository
import kotlinx.coroutines.launch

class TenturncardViewModel(
    private val repository: TenturncardRepository
) : ViewModel() {

    var tenturncards: List<Tenturncard> = emptyList()
        private set

    fun fetchTenturncards() {
        viewModelScope.launch {
            tenturncards = repository.getTenturncards()
        }
    }
}
