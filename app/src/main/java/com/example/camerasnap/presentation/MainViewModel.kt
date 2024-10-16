package com.example.camerasnap.presentation

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.camerasnap.domain.Camera
import com.example.camerasnap.domain.usecases.GetTimeStampFlowUseCase
import com.example.camerasnap.domain.usecases.MakeAndSavePhotoUseCase
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ViewModel"

class MainViewModel(
    getTimeStampFlow: GetTimeStampFlowUseCase,
    private val makeAndSavePhoto: MakeAndSavePhotoUseCase,
    private val camera: Camera
) : ViewModel() {

    private val _state = MutableStateFlow(MainState.EMPTY)
    val state: StateFlow<MainState> get() = _state

    init {
        observeTimeStampFlow(getTimeStampFlow)
    }

    private fun observeTimeStampFlow(getTimeStampFlow: GetTimeStampFlowUseCase) {
        getTimeStampFlow()
            .onEach { timeStamp ->
                Log.d(TAG, "Received timestamp: $timeStamp")
                _state.value = _state.value.copy(timeStamp = timeStamp.toString())
            }
            .launchIn(viewModelScope)
    }

    fun takeAndSavePhoto() {
        viewModelScope.launch {
            try {
                makeAndSavePhoto()
                Log.d(TAG, "Photo taken and saved successfully.")
            } catch (e: Exception) {
                Log.e(TAG, "Error taking photo: ${e.message}", e)
            }
        }
    }

    fun initCamera(activity: AppCompatActivity, previewView: PreviewView) {
        camera.initCamera(activity, previewView)
    }
}

class MainViewModelFactory @Inject constructor(
    private val getTimeStampFlow: Lazy<GetTimeStampFlowUseCase>,
    private val makeAndSavePhoto: Lazy<MakeAndSavePhotoUseCase>,
    private val camera: Lazy<Camera>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(getTimeStampFlow.get(), makeAndSavePhoto.get(), camera.get()) as T
    }
}