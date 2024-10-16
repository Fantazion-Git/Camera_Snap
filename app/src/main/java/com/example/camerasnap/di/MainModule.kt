package com.example.camerasnap.di

import android.content.ContentResolver
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import com.example.camerasnap.MainActivity
import com.example.camerasnap.data.CameraImpl
import com.example.camerasnap.domain.Camera
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
    fun provideContentResolver(activity: MainActivity): ContentResolver = activity.contentResolver
}