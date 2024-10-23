package com.example.mylibrary.domain

import androidx.camera.core.ImageAnalysis.Analyzer
import androidx.camera.core.ImageProxy

class TimeStampAnalyzer(private val callback: (Long) -> Unit) : Analyzer {

    companion object {
        fun newInstance(callback: (Long) -> Unit) = TimeStampAnalyzer(callback)
    }

    override fun analyze(image: ImageProxy) {
        val timestamp = image.imageInfo.timestamp
        image.close()
        callback(timestamp)
    }
}