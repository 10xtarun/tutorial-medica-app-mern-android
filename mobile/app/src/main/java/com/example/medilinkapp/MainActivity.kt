package com.example.medilinkapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medilinkapp.ui.GreetingScreen
import com.example.medilinkapp.viewmodel.GreetingViewModel

/**
 * Main activity of the application
 * Manages the navigation between greeting screen and main content
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // State to control whether to show greeting or main content
            var showGreeting by remember { mutableStateOf(true) }
            // Initialize the ViewModel
            val viewModel: GreetingViewModel = viewModel()

            // Show either greeting screen or main content based on state
            if (showGreeting) {
                GreetingScreen(
                    viewModel = viewModel,
                    onGreetingComplete = { showGreeting = false }
                )
            } else {
                // Your main app content will go here
                // For now, we'll just show a placeholder
                androidx.compose.material3.Text("Main App Content")
            }
        }
    }
}