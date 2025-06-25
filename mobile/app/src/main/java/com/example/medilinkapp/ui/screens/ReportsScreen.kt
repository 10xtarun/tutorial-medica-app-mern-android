package com.example.medilinkapp.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.medilinkapp.data.TokenManager
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.request.forms.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
import io.ktor.http.content.PartData
import io.ktor.http.Headers
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.Input

/**
 * Reports screen composable
 *
 * Section 1: Upload photos from gallery (image picker and upload)
 * Section 2: Trigger analysis (button)
 * Section 3: Show analysis result (placeholder)
 */
@Composable
fun ReportsScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadStatus by remember { mutableStateOf<String?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var appointmentId by remember { mutableStateOf("") }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        uploadStatus = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Section 1: Upload photos from gallery
        Text(text = "Section 1: Upload Photos (Gallery)")
        OutlinedTextField(
            value = appointmentId,
            onValueChange = { appointmentId = it },
            label = { Text("Appointment ID") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Select Image")
        }
        selectedImageUri?.let { uri ->
            Spacer(modifier = Modifier.height(8.dp))
            // Show image preview
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected Image",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        isUploading = true
                        uploadStatus = uploadImageToServerWithAuth(context, uri, appointmentId)
                        isUploading = false
                    }
                },
                enabled = !isUploading && appointmentId.isNotBlank()
            ) {
                Text(if (isUploading) "Uploading..." else "Upload Image")
            }
            uploadStatus?.let {
                Text(text = it)
            }
        }

        Divider()

        // Section 2: Trigger analysis
        Text(text = "Section 2: Trigger Analysis")
        Button(onClick = { /* TODO: Trigger analysis */ }) {
            Text("Analyze")
        }

        Divider()

        // Section 3: Show analysis result
        Text(text = "Section 3: Analysis Result")
        // TODO: Show analysis result here
    }
}

// Helper function to upload image to the server using Ktor with Authorization header
suspend fun uploadImageToServerWithAuth(context: Context, uri: Uri, appointmentId: String): String {
    return try {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return "Failed to open image."
        val fileName = getFileName(context, uri) ?: "image.jpg"
        val tempFile = java.io.File(context.cacheDir, fileName)
        tempFile.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
        val tokenManager = com.example.medilinkapp.data.TokenManager(context)
        val accessToken = tokenManager.accessToken.first()
        if (accessToken.isNullOrBlank()) return "No access token found. Please login."
        val client = io.ktor.client.HttpClient(io.ktor.client.engine.android.Android) {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                (kotlinx.serialization.json.Json { ignoreUnknownKeys = true; isLenient = true })
            }
        }
        val imageBytes = tempFile.readBytes()
        val response: io.ktor.client.statement.HttpResponse = client.submitFormWithBinaryData(
            url = "https://bd16-150-107-16-195.ngrok-free.app/v1/reports/upload",
            formData = io.ktor.client.request.forms.formData {
                append("appointmentId", appointmentId)
                append("reportFile", imageBytes, Headers.build {
                    append(io.ktor.http.HttpHeaders.ContentType, "image/jpeg")
                    append(io.ktor.http.HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }
        ) {
            headers {
                append(io.ktor.http.HttpHeaders.Authorization, accessToken)
            }
        }
        if (response.status.isSuccess()) {
            "Upload successful!"
        } else {
            "Upload failed: ${response.status.description}"
        }
    } catch (e: Exception) {
        "Upload failed: ${e.localizedMessage}"
    }
}

// Helper to get file name from Uri
fun getFileName(context: Context, uri: Uri): String? {
    var name: String? = null
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index >= 0) name = it.getString(index)
        }
    }
    return name
} 