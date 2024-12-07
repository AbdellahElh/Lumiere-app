package com.example.riseandroid.ui.screens.scanner


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.riseandroid.LumiereApplication
import com.journeyapps.barcodescanner.ScanOptions

// the ViewModel cannot directly access LifecycleOwner or lifecycle-bound APIs like registerForActivityResult,
// we must provide a mechanism to delegate these actions to the ViewModel but still invoke them from the Activity where required.
// Have a look at the MainActivity class to get a better understanding of why this is used.
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

class ScannerViewModel : ViewModel() {
    val scannerState = MutableStateFlow<ScannerState>(ScannerState.Loading)

    val actionState = MutableStateFlow<ScannerAction>(ScannerAction.RequestCameraPermission)

    fun onScanResult(contents: String?) {
        if (contents == null) {
            scannerState.value = ScannerState.Cancelled
        } else {
            scannerState.value = ScannerState.ShowResult(contents)
        }
    }

    fun checkCameraPermission(isPermissionGranted: Boolean) {
        if (isPermissionGranted) {
            actionState.value = ScannerAction.LaunchScanner
        } else {
            actionState.value = ScannerAction.RequestCameraPermission
        }
    }

    fun requestCameraPermission() {
        actionState.value = ScannerAction.RequestCameraPermission
    }

    fun showToast(message: String) {
        scannerState.value = ScannerState.ShowToast(message)
    }

    fun launchScanner() {
        actionState.value = ScannerAction.LaunchScanner
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LumiereApplication)
                ScannerViewModel()
            }
        }
    }
}
