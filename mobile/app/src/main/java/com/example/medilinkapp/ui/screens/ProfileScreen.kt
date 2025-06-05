package com.example.medilinkapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medilinkapp.network.models.UserRole
import com.example.medilinkapp.viewmodel.AuthViewModel

/**
 * Profile screen composable with authentication forms
 * 
 * Why: Handles user authentication and profile management
 * - Provides login and signup functionality
 * - Allows user role selection
 * - Shows authentication status
 * - Manages token-based visibility
 */
@Composable
fun ProfileScreen() {
    val viewModel: AuthViewModel = viewModel()
    val showSignup by viewModel.showSignup.collectAsState()
    val selectedRole by viewModel.selectedRole.collectAsState()
    val error by viewModel.error.collectAsState()
    val authResult by viewModel.authResult.collectAsState()
    val hasToken by viewModel.hasToken.collectAsState()

    // Form state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Show error if any
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Show success message if authenticated
        authResult?.let {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Welcome, ${it.name}!",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Email: ${it.email}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Role: ${it.role}",
                    style = MaterialTheme.typography.bodyLarge
                )
                if (!it.verifiedEmail) {
                    Text(
                        text = "Please verify your email",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        if (hasToken) {
            // Show logout button when token is present
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Logout")
            }
        } else {
            // Show authentication forms when no token is present
            // Role selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                UserRole.values().forEach { role ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedRole == role,
                            onClick = { viewModel.updateRole(role) }
                        )
                        Text(role.value.capitalize())
                    }
                }
            }

            // Form fields
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            // Show name field only in signup
            if (showSignup) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            // Action buttons
            Button(
                onClick = {
                    if (showSignup) {
                        viewModel.signup(email, password, name)
                    } else {
                        viewModel.login(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(if (showSignup) "Sign Up" else "Login")
            }

            // Toggle between login and signup
            TextButton(
                onClick = { viewModel.toggleSignup() }
            ) {
                Text(
                    if (showSignup) "Already have an account? Login"
                    else "New user? Sign up"
                )
            }
        }
    }
} 