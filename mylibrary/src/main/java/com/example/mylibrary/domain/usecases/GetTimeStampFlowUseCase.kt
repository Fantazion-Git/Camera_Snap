package com.example.mylibrary.domain.usecases

import com.example.mylibrary.domain.Camera
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.sample
import javax.inject.Inject

private const val DELAY_GET_CURRENT_TIMESTAMP_MS = 5000L

class GetTimeStampFlowUseCase @Inject constructor(
    private val camera: Camera
) {

    @OptIn(FlowPreview::class)
    operator fun invoke(): Flow<Long> = camera.timestampFlow
        .sample(DELAY_GET_CURRENT_TIMESTAMP_MS)
        .onStart { emit(camera.timestampFlow.first()) }
}