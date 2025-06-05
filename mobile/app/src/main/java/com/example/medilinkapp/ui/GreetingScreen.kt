package com.example.medilinkapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.medilinkapp.viewmodel.GreetingViewModel
import kotlinx.coroutines.delay

/**
 * Composable function for the greeting screen
 * Displays a welcome message for 2 seconds before transitioning to main content
 * 
 * @param viewModel ViewModel that provides the greeting message
 * @param onGreetingComplete Callback to be invoked after 2 seconds
 */
@Composable
fun GreetingScreen(
    viewModel: GreetingViewModel,
    onGreetingComplete: () -> Unit
) {
    // Collect the greeting message from ViewModel
    val greeting by viewModel.greeting.collectAsState()

    // Launch a coroutine to handle the 2-second delay
    LaunchedEffect(key1 = true) {
        delay(2000) // Show for 2 seconds
        onGreetingComplete() // Notify parent that greeting is complete
    }

    // Center the greeting message on screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = greeting,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
} 