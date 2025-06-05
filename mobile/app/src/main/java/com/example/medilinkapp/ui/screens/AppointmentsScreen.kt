package com.example.medilinkapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Appointments screen composable
 * 
 * Why: Manages and displays medical appointments
 * - Shows upcoming appointments
 * - Displays past appointment history
 * - Helps users track their medical visits
 * 
 * Implementation Details:
 * - Uses Box for centering content
 * - Currently shows placeholder text
 * - Can be expanded to show appointment list/calendar
 * 
 * Properties Used:
 * - Modifier.fillMaxSize(): Makes the Box take up all available space
 * - Alignment.Center: Centers the content both horizontally and vertically
 */
@Composable
fun AppointmentsScreen() {
    // Box composable for layout
    // Why: Provides a container that can position its children
    // - Allows for precise control over child positioning
    // - Can center content using contentAlignment
    Box(
        modifier = Modifier.fillMaxSize(),  // Makes the Box take full screen size
        contentAlignment = Alignment.Center // Centers the content
    ) {
        // Text composable for displaying content
        // Why: Shows the screen's content to the user
        // - Currently shows placeholder text
        // - Can be replaced with appointments UI
        Text(text = "Appointments Screen")
    }
} 