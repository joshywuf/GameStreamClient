package com.example.gamestream.net

import java.io.IOException
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.NoRouteToHostException
import java.net.Socket
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val DEFAULT_PORT = 47800
private const val CONNECT_TIMEOUT_MS = 3000
private const val READ_TIMEOUT_MS = 3000
private const val CLIENT_HELLO = "HELLO gamestream-client 1"
private const val HOST_HELLO_PREFIX = "HELLO gamestream-host"

/**
 * Step 2 connectivity test. Opens a TCP connection, sends one greeting line and
 * reports Connected only if the host answers with the expected greeting.
 * This is not the pairing protocol and carries no authentication.
 */
class TcpHostConnection(
    private val scope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : HostConnection {
    private val _state = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    override val state: StateFlow<ConnectionState> = _state.asStateFlow()
    private var job: Job? = null

    override fun connect(host: String) {
        job?.cancel()
        val target = parseHostAddress(host)
        if (target == null) {
            _state.value = ConnectionState.Failed(
                "Enter an address like 10.0.2.2 or 10.0.2.2:$DEFAULT_PORT"
            )
            return
        }
        job = scope.launch {
            _state.value = ConnectionState.Connecting
            _state.value = try {
                withContext(ioDispatcher) { handshake(target.first, target.second) }
                ConnectionState.Connected
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                ConnectionState.Failed(describeFailure(e))
            }
        }
    }

    override fun disconnect() {
        job?.cancel()
        _state.value = ConnectionState.Disconnected
    }
}

private fun handshake(host: String, port: Int) {
    val address = java.net.InetAddress.getByName(host)
    Socket().use { socket ->
        socket.connect(InetSocketAddress(address, port), CONNECT_TIMEOUT_MS)
        socket.soTimeout = READ_TIMEOUT_MS
        val writer = socket.getOutputStream().bufferedWriter(Charsets.UTF_8)
        writer.write(CLIENT_HELLO)
        writer.newLine()
        writer.flush()
        val reader = socket.getInputStream().bufferedReader(Charsets.UTF_8)
        val reply = reader.readLine()
            ?: throw IOException("The host closed the connection without answering")
        if (!reply.startsWith(HOST_HELLO_PREFIX)) {
            throw IOException("The host answered, but not with the expected greeting")
        }
    }
}

private fun describeFailure(e: Exception): String = when (e) {
    is SocketTimeoutException ->
        "Timed out (${e.message}). Check the address and that the PC test script is running."
    is ConnectException, is NoRouteToHostException ->
        "Could not reach host (${e.message}). Check the address and that the PC test script is running."
    is UnknownHostException ->
        "The address could not be resolved (${e.message})."
    else -> "${e.javaClass.simpleName}: ${e.message}"
}

internal fun parseHostAddress(input: String, defaultPort: Int = DEFAULT_PORT): Pair<String, Int>? {
    val text = input.trim()
    if (text.isEmpty()) return null
    val parts = text.split(":")
    return when (parts.size) {
        1 -> text to defaultPort
        2 -> {
            val port = parts[1].toIntOrNull()
            if (parts[0].isBlank() || port == null || port !in 1..65535) null else parts[0] to port
        }
        else -> null
    }
}
