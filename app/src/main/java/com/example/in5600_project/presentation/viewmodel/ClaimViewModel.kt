package com.example.in5600_project.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ClaimViewModel : ViewModel() {
    // State for the claim description.
    var description = mutableStateOf("")
        private set

    // State for the claim location.
    var location = mutableStateOf("")
        private set

    // State for the selected claim status.
    var selectedStatus = mutableStateOf("Pending")
        private set

    // List of available status options.
    val statusOptions = listOf("Pending", "Approved", "Rejected")

    // State for the selected image URI.
    var imageUri = mutableStateOf<Uri?>(null)
        private set

    // Update functions for each state.
    fun onDescriptionChanged(newDescription: String) {
        description.value = newDescription
    }

    fun onLocationChanged(newLocation: String) {
        location.value = newLocation
    }

    fun onStatusSelected(newStatus: String) {
        selectedStatus.value = newStatus
    }

    fun onImageUriChanged(newUri: Uri?) {
        imageUri.value = newUri
    }
}