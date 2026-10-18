package com.example.gamestream.net

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ConnectionState {
    data object Disconnected : ConnectionState
    data object Connecting : ConnectionState
    data object Connected : ConnectionState
    data class Failed(val reason: String) : ConnectionState
}

interface HostConnection {
    val state: StateFlow<ConnectionState>
    fun connect(host: String)
    fun disconnect()
}

/** UI-testing stand-in. Replaced by a real LAN connection in a later step. */
class FakeHostConnection(private val scope: CoroutineScope) : HostConnection {
    private val _state = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    override val state: StateFlow<ConnectionState> = _state.asStateFlow()
    private var job: Job? = null

    override fun connect(host: String) {
        job?.cancel()
        job = scope.launch {
            _state.value = ConnectionState.Connecting
            delay(1500)
            _state.value = if (host.isBlank()) {
                ConnectionState.Failed("No host address entered")
            } else {
                ConnectionState.Connected
            }
        }
    }

    override fun disconnect() {
        job?.cancel()
        _state.value = ConnectionState.Disconnected
    }
}
