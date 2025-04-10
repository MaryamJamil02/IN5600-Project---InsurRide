package com.example.in5600_project.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in5600_project.data.datastore.ClaimInformation
import com.example.in5600_project.data.network.getMethodDownloadPhoto
import com.example.in5600_project.util.decodeBase64ToUri
import kotlinx.coroutines.launch

class ClaimInfoViewModel : ViewModel() {
    var description = mutableStateOf("")
        private set
    var location = mutableStateOf("")
        private set
    var status = mutableStateOf("Pending")
        private set
    // The photo variable now stores the URI (as string) returned from the decoded file.
    var photo = mutableStateOf("")
        private set
    var isEditMode = mutableStateOf(false)
        private set

    // List of available status options.
    val statusOptions = listOf("Pending", "Approved", "Rejected")

    fun enterEditMode(claim: ClaimInformation) {
        description.value = claim.claimDes
        location.value = claim.claimLocation
        status.value = claim.claimStatus
        // Clear any previous value – we’ll fetch the updated photo from the server.
        photo.value = ""
        isEditMode.value = true
    }

    fun exitEditMode() {
        isEditMode.value = false
    }

    fun onDescriptionChanged(newDescription: String) {
        description.value = newDescription
    }

    fun onLocationChanged(newLocation: String) {
        location.value = newLocation
    }

    fun onStatusChanged(newStatus: String) {
        status.value = newStatus
    }

    fun onPhotoChanged(newPhoto: String) {
        photo.value = newPhoto
    }

    // Function to fetch the Base64 string from the server, decode it and update photo state.
    fun fetchPhoto(context: Context, fileName: String) {
        viewModelScope.launch {
            val base64String = getMethodDownloadPhoto(context, fileName)
            base64String?.let {
                val uri = decodeBase64ToUri(context, it, fileName)
                uri?.let { decodedUri ->
                    onPhotoChanged(decodedUri.toString())
                }
            }
        }
    }
}
