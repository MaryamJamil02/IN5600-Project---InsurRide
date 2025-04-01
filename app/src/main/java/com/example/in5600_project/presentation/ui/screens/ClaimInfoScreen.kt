package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.in5600_project.data.datastore.ClaimInformation
import com.example.in5600_project.presentation.viewmodel.ClaimInfoViewModel
import androidx.navigation.NavController

@Composable
fun ClaimInfoScreen(
    modifier: Modifier,
    claim: ClaimInformation,
    navController: NavController,
    viewModel: ClaimInfoViewModel
) {
    if (!viewModel.isEditMode.value) {
        // View mode: show claim details and an "Edit Claim" button.
        Column(modifier = modifier
            .fillMaxSize()
            .padding(16.dp)) {
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
        Column(modifier = modifier
            .fillMaxSize()
            .padding(16.dp)) {
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
            OutlinedTextField(
                value = viewModel.status.value,
                onValueChange = { viewModel.onStatusChanged(it) },
                label = { Text("Status") },
                modifier = modifier.fillMaxWidth()
            )
            Spacer(modifier = modifier.height(16.dp))
            Button(onClick = {
                // Implement update logic (e.g., update the claim in your DataStore and on the server)

                navController.navigate("claimsHomeScreen")
                // Optionally, navigate back or show a toast indicating success.
            }) {
                Text("Update Claim")
            }
        }
    }
}
