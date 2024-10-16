package com.example.camerasnap.domain

import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import kotlinx.coroutines.flow.Flow

interface Camera {
    val timestampFlow: Flow<Long>
    fun initCamera(activity: AppCompatActivity, previewView: PreviewView)
    fun makeAndSavePhoto()
    suspend fun getImage(): ImageProxy
}