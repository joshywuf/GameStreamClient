package com.example.gamestream.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamestream.net.ConnectionState
import com.example.gamestream.net.HostConnection
import com.example.gamestream.net.TcpHostConnection
import kotlinx.coroutines.flow.StateFlow

class ConnectionViewModel : ViewModel() {
    private val connection: HostConnection = TcpHostConnection(viewModelScope)

    val state: StateFlow<ConnectionState> = connection.state

    var host by mutableStateOf("127.0.0.1")
        private set

    fun onHostChange(value: String) {
        host = value
    }

    fun connect() = connection.connect(host)

    fun disconnect() = connection.disconnect()
}
