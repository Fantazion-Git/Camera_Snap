package com.example.mylibrary.presentation

data class MainState(

    val timeStamp: String
) {
    companion object {
        val EMPTY = MainState("")
    }
}