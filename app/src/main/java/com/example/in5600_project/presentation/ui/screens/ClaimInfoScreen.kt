package com.example.in5600_project.presentation.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.in5600_project.data.datastore.ClaimInformation
import com.example.in5600_project.presentation.viewmodel.ClaimInfoViewModel
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.ClaimsManager
import com.example.in5600_project.data.network.postUpdateClaim
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun ClaimInfoScreen(
    modifier: Modifier,
    claim: ClaimInformation,
    navController: NavController,
    viewModel: ClaimInfoViewModel,
    context: Context,
    userId: String
) {
    val coroutineScope = rememberCoroutineScope()
    val claimsManager = ClaimsManager(context)

    var expanded by remember { mutableStateOf(false) }

    // Launcher to pick an image from the gallery
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        //viewModel.onPhotoChanged(uri)
    }

    if (!viewModel.isEditMode.value) {
        // View mode: show claim details and an "Edit Claim" button.
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Claim ID: ${claim.claimId}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = modifier.height(4.dp))
            AsyncImage(
                model = claim.claimPhoto,
                contentDescription = "Claim Photo",
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = modifier.height(8.dp))

            Text(
                text = "Status: ${claim.claimStatus}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = modifier.height(4.dp))
            Text(
                text = "Description: ${claim.claimDes}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = modifier.height(4.dp))
            Text(
                text = "Location: ${claim.claimLocation}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = modifier.height(16.dp))
            Button(onClick = {
                viewModel.enterEditMode(claim)
            }) {
                Text("Edit Claim")
            }
        }
    } else {
        // Edit mode: show editable fields pre-filled with the claim info.
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AsyncImage(
                model = viewModel.photo.value,
                contentDescription = "Claim Photo",
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.description.value,
                onValueChange = { viewModel.onDescriptionChanged(it) },
                label = { Text("Description") },
                modifier = modifier.fillMaxWidth()
            )
            Spacer(modifier = modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.location.value,
                onValueChange = { viewModel.onLocationChanged(it) },
                label = { Text("Location") },
                modifier = modifier.fillMaxWidth()
            )
            Spacer(modifier = modifier.height(8.dp))

            // Dropdown for Status
            Box(modifier = modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = viewModel.status.value,
                    onValueChange = {},
                    label = { Text("Status") },
                    modifier = modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select Status"
                            )
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = modifier.fillMaxWidth()
                ) {
                    viewModel.statusOptions.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status) },
                            onClick = {
                                viewModel.onStatusChanged(status)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = modifier.height(16.dp))

            // Button to select a photo from the gallery
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = modifier.fillMaxWidth()
            ) {
                Text("Select Photo")
            }

            // Display a preview of the selected image, if available
            viewModel.photo.value.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "Selected Photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }



            Spacer(modifier = modifier.height(16.dp))
            Button(onClick = {
                // Implement update logic (e.g., update the claim in your DataStore and on the server)
                coroutineScope.launch {
                    val numberOfClaims = claimsManager.getNumberOfClaims(userId).first()

                    val responseUpdatedClaim = postUpdateClaim(
                        context = context,
                        userId = userId,
                        indexUpdateClaim = claim.claimId,
                        updateClaimDescription = viewModel.description.value,
                        updateClaimPhoto = viewModel.photo.value, // LATERFIX
                        updateClaimLocation = viewModel.location.value,
                        updateClaimStatus = viewModel.status.value
                    )

                    if (responseUpdatedClaim != null) {
                        // Update the claim in your DataStore
                        // Create a new ClaimInformation object.
                        val updatedClaim = ClaimInformation(
                            claimId = claim.claimId,
                            claimDes = viewModel.description.value,
                            claimPhoto = viewModel.photo.value,
                            claimLocation = viewModel.location.value,
                            claimStatus = viewModel.status.value
                        )

                        // Insert the new claim locally.
                        claimsManager.updateOrInsertClaimAtIndex(
                            userId,
                            claim.claimId.toInt(),
                            updatedClaim,
                            false
                        )

                        Toast.makeText(context, "Claim updated successfully", Toast.LENGTH_SHORT)
                            .show()

                        navController.navigate("claimsHomeScreen")
                        viewModel.exitEditMode()
                    } else {
                        Toast.makeText(context, "Failed to update claim", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }) {
                Text("Update Claim")
            }
        }
    }
}




