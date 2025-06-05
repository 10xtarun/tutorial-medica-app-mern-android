package com.example.medilinkapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.medilinkapp.ui.screens.AppointmentsScreen
import com.example.medilinkapp.ui.screens.ProfileScreen
import com.example.medilinkapp.ui.screens.ReportsScreen

/**
 * Sealed class representing different navigation destinations in the app
 * 
 * Why: Sealed classes are used for representing a closed set of types
 * - Ensures type safety
 * - Makes it impossible to create new screen types outside this class
 * - Helps with exhaustive when expressions
 * 
 * Properties:
 * - route: String - Unique identifier for the screen
 * - title: String - Display name of the screen
 */
sealed class Screen(val route: String, val title: String) {
    object Reports : Screen("reports", "Reports")
    object Appointments : Screen("appointments", "Appointments")
    object Profile : Screen("profile", "Profile")
}

/**
 * Main navigation component that handles screen navigation and bottom bar
 * 
 * Why: This composable manages the app's navigation state and UI
 * - Provides a consistent navigation experience
 * - Handles screen switching
 * - Manages bottom navigation bar state
 * 
 * Implementation Details:
 * - Uses Scaffold for proper Material Design layout
 * - Manages navigation state using remember and mutableStateOf
 * - Implements bottom navigation using NavigationBar
 */
@Composable
fun MainNavigation() {
    // State management for selected screen
    // Why: Tracks which screen is currently active
    // - Uses remember to maintain state across recompositions
    // - Uses mutableStateOf to trigger recomposition when changed
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Reports) }

    // Navigation items configuration
    // Why: Defines the structure of bottom navigation items
    // - Each item has a screen, icon, and label
    // - Uses Triple to group related data
    val items = listOf(
        Triple(Screen.Reports, Icons.Default.Warning, "Reports"),
        Triple(Screen.Appointments, Icons.Default.DateRange, "Appointments"),
        Triple(Screen.Profile, Icons.Default.Person, "Profile")
    )

    // Scaffold provides the basic Material Design visual layout structure
    // Why: Ensures proper positioning of UI elements
    // - Manages system insets
    // - Provides proper spacing and padding
    // - Handles bottom navigation bar positioning
    Scaffold(
        // Bottom navigation bar configuration
        bottomBar = {
            // NavigationBar provides the container for navigation items
            // Why: Creates a consistent bottom navigation experience
            // - Handles item selection
            // - Manages visual feedback
            NavigationBar {
                items.forEach { (screen, icon, label) ->
                    // NavigationBarItem represents a single navigation option
                    // Why: Provides interactive navigation elements
                    // - Shows icon and label
                    // - Handles selection state
                    // - Manages click events
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen }
                    )
                }
            }
        }
    ) { paddingValues ->
        // Screen content based on selection
        // Why: Shows the appropriate screen based on user selection
        // - Uses when expression for type-safe screen switching
        // - Applies padding from Scaffold
        when (selectedScreen) {
            Screen.Reports -> ReportsScreen()
            Screen.Appointments -> AppointmentsScreen()
            Screen.Profile -> ProfileScreen()
        }
    }
} 