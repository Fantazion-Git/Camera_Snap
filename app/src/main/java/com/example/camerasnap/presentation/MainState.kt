package com.example.camerasnap.presentation

data class MainState(

    val timeStamp: String
) {
    companion object {
        val EMPTY = MainState("")
    }
}