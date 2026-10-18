package com.example.gamestream.input

data class TouchEventData(
    val pointerId: Int,
    val x: Float,
    val y: Float,
    val action: Int
)

interface InputSender {
    fun sendTouch(event: TouchEventData)

    /** Must release every held input. Called on disconnect or focus loss. */
    fun releaseAll()
}
