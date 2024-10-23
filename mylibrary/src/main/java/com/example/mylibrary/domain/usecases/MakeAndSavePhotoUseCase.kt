package com.example.mylibrary.domain.usecases

import com.example.mylibrary.domain.Camera
import javax.inject.Inject

class MakeAndSavePhotoUseCase @Inject constructor(private val camera: Camera) {
    operator fun invoke() = camera.makeAndSavePhoto()
}