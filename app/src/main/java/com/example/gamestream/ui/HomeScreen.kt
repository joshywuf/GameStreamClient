package com.example.gamestream.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gamestream.net.ConnectionState
import com.example.gamestream.net.DEFAULT_PORT

@Composable
fun HomeScreen(viewModel: ConnectionViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Game Stream Client", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = viewModel.host,
            onValueChange = viewModel::onHostChange,
            label = { Text("PC address") },
            supportingText = { Text("Emulator: 10.0.2.2. Port $DEFAULT_PORT is used if none is given.") },
            singleLine = true
        )
        Spacer(Modifier.height(16.dp))

        Text("Status: ${describe(state)}")
        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = viewModel::connect) { Text("Connect") }
            OutlinedButton(onClick = viewModel::disconnect) { Text("Disconnect") }
        }
        Spacer(Modifier.height(24.dp))

        Text(
            "Connection test only. Nothing is streamed yet.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun describe(state: ConnectionState): String = when (state) {
    ConnectionState.Disconnected -> "Not connected"
    ConnectionState.Connecting -> "Connecting..."
    ConnectionState.Connected -> "Connected"
    is ConnectionState.Failed -> "Failed: ${state.reason}"
}
