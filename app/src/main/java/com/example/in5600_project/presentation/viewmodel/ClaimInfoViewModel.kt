package com.example.in5600_project.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.in5600_project.data.datastore.ClaimInformation

class ClaimInfoViewModel : ViewModel() {
   /* var claimId = mutableStateOf("")
        private set*/
    var description = mutableStateOf("")
        private set
    var location = mutableStateOf("")
        private set
    var status = mutableStateOf("")
        private set
    var photo = mutableStateOf("")
        private set
    var isEditMode = mutableStateOf(false)
        private set

    fun enterEditMode(claim: ClaimInformation) {
        //claimId.value = claim.claimId
        description.value = claim.claimDes
        location.value = claim.claimLocation
        status.value = claim.claimStatus
        photo.value = claim.claimPhoto
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

}
