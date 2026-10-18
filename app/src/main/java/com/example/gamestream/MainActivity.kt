package com.example.gamestream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamestream.ui.ConnectionViewModel
import com.example.gamestream.ui.HomeScreen
import com.example.gamestream.ui.theme.GameStreamClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameStreamClientTheme {
                val connectionViewModel: ConnectionViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(connectionViewModel, Modifier.padding(innerPadding))
                }
            }
        }
    }
}
