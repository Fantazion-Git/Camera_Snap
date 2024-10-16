package com.example.camerasnap.data

import android.content.ContentResolver
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.camerasnap.domain.Camera
import com.example.camerasnap.domain.TimeStampAnalyzer
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val TAG = "PhotoMakerImpl"
private const val MIME_TYPE = "image/jpeg"
private const val RELATIVE_PATH = "Pictures/CameraX-Image"

class CameraImpl(
    private val imageCapture: ImageCapture,
    private val preview: Preview,
    private val executor: ExecutorService,
    private val contentResolver: ContentResolver
) : Camera {

    private val _timestampFlow = MutableSharedFlow<Long>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val timestampFlow: Flow<Long> get() = _timestampFlow

    override fun makeAndSavePhoto() {
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val contentValues = createContentValues(name)

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            .build()

        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Log.d(TAG, "Photo capture succeeded: ${output.savedUri}")
                }
            }
        )
    }

    private fun createContentValues(name: String): ContentValues {
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE)
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    put(MediaStore.Images.Media.RELATIVE_PATH, RELATIVE_PATH)
                }
            }
        }
    }

    override suspend fun getImage(): ImageProxy = suspendCancellableCoroutine { cont ->
        imageCapture.takePicture(
            executor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    cont.resume(imageProxy)
                }

                override fun onError(exception: ImageCaptureException) {
                    cont.resumeWithException(exception)
                }
            }
        )
    }

    override fun initCamera(activity: AppCompatActivity, previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            preview.surfaceProvider = previewView.surfaceProvider

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val analysis = ImageAnalysis.Builder().build().apply {
                setAnalyzer(executor, TimeStampAnalyzer.newInstance {
                    _timestampFlow.tryEmit(it)
                })
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(activity, cameraSelector, preview, imageCapture, analysis)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(activity))
    }
}