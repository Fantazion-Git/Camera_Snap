package com.example.mylibrary.di

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.fragment.app.FragmentActivity
import com.example.mylibrary.data.CameraImpl
import com.example.mylibrary.domain.Camera
import dagger.Module
import dagger.Provides
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class MainModule {

    @Singleton
    @Provides
    fun providePhotoMaker(
        imageCapture: ImageCapture,
        preview: Preview,
        executor: ExecutorService,
        contentResolver: ContentResolver,
    ): Camera = CameraImpl(imageCapture, preview, executor, contentResolver)

    @Singleton
    @Provides
    fun provideImageCapture(): ImageCapture = ImageCapture.Builder().build()

    @Singleton
    @Provides
    fun providePreview(): Preview = Preview.Builder().build()

    @Singleton
    @Provides
    fun provideExecutor(): ExecutorService = Executors.newSingleThreadExecutor()

    @Singleton
    @Provides
    fun provideContentResolver(fragmentActivity: FragmentActivity): ContentResolver = fragmentActivity.contentResolver
}