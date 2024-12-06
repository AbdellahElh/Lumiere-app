package com.example.riseandroid.ui.screens.scanner


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


sealed interface ScannerState {
    data class ShowResult(val result: String) : ScannerState
    object Cancelled : ScannerState
    object RequestCameraPermission : ScannerState
    data class ShowToast(val message: String) : ScannerState
}

class ScannerViewModel : ViewModel() {
    private val _state = MutableStateFlow<ScannerState>(ScannerState.Cancelled)
    val state: StateFlow<ScannerState> = _state

    fun onScanResult(contents: String?) {
        if (contents == null) {
            _state.value = ScannerState.Cancelled
        } else {
            _state.value = ScannerState.ShowResult(contents)
        }
    }

    fun requestCameraPermission() {
        _state.value = ScannerState.RequestCameraPermission
    }

    fun showToast(message: String) {
        _state.value = ScannerState.ShowToast(message)
    }
}