package com.example.in5600_project.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in5600_project.data.datastore.ClaimInformation
import com.example.in5600_project.data.network.getMethodDownloadPhoto
import com.example.in5600_project.utils.decodeBase64ToUri
import kotlinx.coroutines.launch

class ClaimInfoViewModel : ViewModel() {
    var description = mutableStateOf("")
        private set

    // This is what we actually send to the server for updated claim coords
    var location = mutableStateOf("")
        private set

    var status = mutableStateOf("Pending")
        private set

    // Store the full Uri (content://...) so we can display and read from it.
    var photo = mutableStateOf("")
        private set

    var isEditMode = mutableStateOf(false)
        private set

    // List of available status options.
    val statusOptions = listOf("Pending", "Approved", "Rejected")

    fun enterEditMode(claim: ClaimInformation) {
        description.value = claim.claimDes
        location.value = claim.claimLocation  // Use the existing location from the claim
        status.value = claim.claimStatus
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

    fun onPhotoChanged(newPhotoUri: String) {
        photo.value = newPhotoUri
    }

    fun fetchPhoto(context: Context, fileName: String) {
        viewModelScope.launch {
            val base64String = getMethodDownloadPhoto(context, fileName)
            base64String?.let {
                val uri = decodeBase64ToUri(context, it, fileName)
                uri?.let { decodedUri ->
                    onPhotoChanged(decodedUri.toString())  // store the full content:// path
                }
            }
        }
    }
}
