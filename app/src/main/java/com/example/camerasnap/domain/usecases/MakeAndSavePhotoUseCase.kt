package com.example.camerasnap.domain.usecases

import com.example.camerasnap.domain.Camera
import javax.inject.Inject

class MakeAndSavePhotoUseCase @Inject constructor(private val camera: Camera) {
    operator fun invoke() = camera.makeAndSavePhoto()
}