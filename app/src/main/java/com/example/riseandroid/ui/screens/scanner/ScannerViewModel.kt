package com.example.riseandroid.ui.screens.scanner


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.ITenturncardRepository
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// the ViewModel cannot directly access LifecycleOwner or lifecycle-bound APIs like registerForActivityResult,
// we must provide a mechanism to delegate these actions to the ViewModel but still invoke them from the Activity where required.
// Have a look at the MainActivity class to get a better understanding of why this is used.
// Define actions related to the scanner
sealed interface ScannerAction {
    object RequestCameraPermission : ScannerAction
    object LaunchScanner : ScannerAction
}

sealed interface ScannerState {
    data class ShowResult(val result: String) : ScannerState
    object Cancelled : ScannerState
    data class ShowToast(val message: String) : ScannerState
    object Loading : ScannerState
}

class ScannerViewModel(
    private val tenturncardRepository: ITenturncardRepository
) : ViewModel() {

    // Exposed state to be observed by the UI (Activity or Fragment)
    private val _scannerState = MutableStateFlow<ScannerState>(ScannerState.Loading)
    val scannerState = _scannerState.asStateFlow()

    var _actionState = mutableStateOf<ScannerAction>(ScannerAction.RequestCameraPermission)
        private set
    // Handle the scan result
    fun onScanResult(contents: String?) {
        if (contents == null) {
            _scannerState.value = ScannerState.Cancelled
        } else {
            _scannerState.value = ScannerState.ShowResult(contents)
            submitCode(contents)
        }
    }

    // Handle the code submission process
    private fun submitCode(code: String) {
        val activationCodeTrimmed = code.trim()
        _scannerState.value = ScannerState.Loading
        viewModelScope.launch {
            try {
                tenturncardRepository.addTenturncard(activationCodeTrimmed).collect { resource ->
                    when (resource) {
                        is ApiResource.Error -> {
                            showToast(resource.message ?: "Something went wrong")
                            _scannerState.value = ScannerState.Cancelled
                        }
                        is ApiResource.Loading -> showToast("Your QR code is being processed...")
                        is ApiResource.Success -> showToast("Card successfully added!")
                        is ApiResource.Initial -> null
                    }
                }
            } catch (e: Exception) {
                showToast(e.message ?: "Something went wrong")
                _scannerState.value = ScannerState.Cancelled
            }
        }
    }

    // Handle camera permission check (received from the UI)
    fun checkCameraPermission(isPermissionGranted: Boolean) {
        if (isPermissionGranted) {
            _actionState.value = ScannerAction.LaunchScanner
        } else {
            _actionState.value = ScannerAction.RequestCameraPermission
        }
    }

    // Display toast messages
    private fun showToast(message: String) {
        _scannerState.value = ScannerState.ShowToast(message)
    }

    // Cancel scan
    fun requestCancelled() {
        _scannerState.value = ScannerState.Cancelled
    }

    // Trigger scanner launch
    fun launchScanner() {
        _actionState.value = ScannerAction.LaunchScanner
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as LumiereApplication
                val tenturncardRepo = application.container.tenturncardRepository
                ScannerViewModel(tenturncardRepository = tenturncardRepo)
            }
        }
    }
}


