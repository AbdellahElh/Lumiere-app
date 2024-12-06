package com.example.riseandroid.ui.screens.scanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// This class is responsible for analyzing the image
class ScannerAnalyzer (
    private val onResult: (state: ScannerViewState, barcode: String) -> Unit
) : ImageAnalysis.Analyzer {
    private val delayForProcessingNextImage = 300L

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val options =
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        val scanner = BarcodeScanning.getClient(options)
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                .let { image ->
                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                onResult(ScannerViewState.Success, barcode.rawValue ?: "")
                            }
                        }
                        .addOnFailureListener {
                            onResult(ScannerViewState.Error, it.message.toString())
                        }
                        .addOnCompleteListener {
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(delayForProcessingNextImage)
                                imageProxy.close()
                            }
                        }
                }
        } else {
            onResult(ScannerViewState.Error, "Image is empty")
        }
    }
}