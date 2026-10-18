package com.example.gamestream.telemetry

interface TelemetrySink {
    fun record(name: String, value: Double)
}
