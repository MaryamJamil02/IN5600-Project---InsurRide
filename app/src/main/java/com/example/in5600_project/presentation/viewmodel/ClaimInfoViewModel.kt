package com.example.in5600_project.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.in5600_project.data.datastore.ClaimInformation

class ClaimInfoViewModel : ViewModel() {
   /* var claimId = mutableStateOf("")
        private set*/

    var description = mutableStateOf("")
        private set

    var location = mutableStateOf("")
        private set

    var status = mutableStateOf("Pending")
        private set

    var imageUri = mutableStateOf<Uri?>(null)
        private set

    var isEditMode = mutableStateOf(false)
        private set

    // List of available status options.
    val statusOptions = listOf("Pending", "Approved", "Rejected")

    fun enterEditMode(claim: ClaimInformation) {
        //claimId.value = claim.claimId
        description.value = claim.claimDes
        location.value = claim.claimLocation
        status.value = claim.claimStatus
        imageUri.value = Uri.parse(claim.claimPhoto)
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

    fun onImageUriChanged(newImageUri: Uri?) {
        imageUri.value = newImageUri
    }

}
