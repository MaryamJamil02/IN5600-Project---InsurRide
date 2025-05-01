package com.example.in5600_project.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NewClaimViewModel : ViewModel() {

    var description = mutableStateOf("")
        private set

    var location = mutableStateOf("")
        private set

    var selectedStatus = mutableStateOf("Pending")
        private set

    // List of available status options.
    val statusOptions = listOf("Pending", "Approved", "Rejected")

    var imageUri = mutableStateOf<Uri?>(null)
        private set

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

    // Reset the form fields
    fun reset() {
        description.value = ""
        location.value = ""
        selectedStatus.value = "Pending"
        imageUri.value = null
    }
}


