package com.example.gamestream.pairing

interface PairingManager {
    fun pair(code: String)
    fun revoke()
    fun isPaired(): Boolean
}
