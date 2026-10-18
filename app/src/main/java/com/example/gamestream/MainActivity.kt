package com.example.gamestream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.gamestream.net.FakeHostConnection
import com.example.gamestream.ui.HomeScreen
import com.example.gamestream.ui.theme.GameStreamClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameStreamClientTheme {
                val scope = rememberCoroutineScope()
                val connection = remember { FakeHostConnection(scope) }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        HomeScreen(connection, Modifier.padding(innerPadding))
                }
            }
        }
    }
}
