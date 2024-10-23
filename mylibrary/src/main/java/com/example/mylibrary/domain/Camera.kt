package com.example.mylibrary.domain

import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.Flow

interface Camera {
    val timestampFlow: Flow<Long>
    fun initCamera(activity: FragmentActivity, previewView: PreviewView)
    fun makeAndSavePhoto()
    suspend fun getImage(): ImageProxy
}