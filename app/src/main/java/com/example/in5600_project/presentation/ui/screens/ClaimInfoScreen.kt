package com.example.in5600_project.presentation.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.in5600_project.data.datastore.ClaimInformation
import com.example.in5600_project.presentation.viewmodel.ClaimInfoViewModel
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.ClaimsManager
import com.example.in5600_project.data.network.getMethodDownloadPhoto
import com.example.in5600_project.data.network.postMethodUploadPhoto
import com.example.in5600_project.data.network.postUpdateClaim
import com.example.in5600_project.presentation.ui.components.GoBackButton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ClaimInfoScreen(
    modifier: Modifier,
    claim: ClaimInformation,
    navController: NavController,
    viewModel: ClaimInfoViewModel = viewModel(),
    context: Context,
    userId: String
) {
    val coroutineScope = rememberCoroutineScope()
    val claimsManager = ClaimsManager(context)

    var expanded by remember { mutableStateOf(false) }

    // Launcher to pick an image from the gallery (only used in edit mode).
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Save the full content:// URI into the ViewModel’s state so we can display it.
        uri?.let { viewModel.onPhotoChanged(it.toString()) }
    }

    // If not editing, fetch the (existing) photo from server if it’s not empty.
    LaunchedEffect(claim.claimPhoto) {
        if (!viewModel.isEditMode.value && claim.claimPhoto.isNotEmpty()) {
            viewModel.fetchPhoto(context, claim.claimPhoto)
        }
    }

    if (!viewModel.isEditMode.value) {
        // --------------------------
        // VIEW MODE
        // --------------------------
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text("Claim Information", modifier = modifier.padding(16.dp))

            Spacer(modifier = modifier.height(30.dp))
            GoBackButton(navController, isPopBackStack = false)
            Spacer(modifier = modifier.height(15.dp))

            Text(
                text = "Claim ID: ${claim.claimId}",
                style = MaterialTheme.typography.titleMedium
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

            // Show the claim image. Typically you'd fetch from server + decode to show it.
            DisplayClaimImage(claim.claimPhoto, context)

            Button(onClick = {
                viewModel.enterEditMode(claim)
                // Also fetch the photo in edit mode if necessary.
                viewModel.fetchPhoto(context, claim.claimPhoto)
            }) {
                Text("Edit Claim")
            }
        }
    } else {
        // --------------------------
        // EDIT MODE
        // --------------------------
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text("Edit Claim Information", modifier = modifier.padding(16.dp))

            Spacer(modifier = modifier.height(30.dp))

            GoBackButton(navController, onReset= { viewModel.exitEditMode() }, isPopBackStack = false)
            Spacer(modifier = modifier.height(15.dp))

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

            // Button to select a new photo from the gallery
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = modifier.fillMaxWidth()
            ) {
                Text("Change Photo")
            }

            // Preview the newly selected image
            if (viewModel.photo.value.isNotEmpty()) {
                AsyncImage(
                    model = viewModel.photo.value,
                    contentDescription = "Selected Photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Spacer(modifier = modifier.height(16.dp))

            // Button to confirm the update
            Button(onClick = {
                coroutineScope.launch {
                    // We keep the full content URI in viewModel.photo.
                    // But for the server, we want to store only the final filename portion:
                    val fullUri = viewModel.photo.value
                    // Extract lastPathSegment, minus extension if you prefer:
                    val cleanedFileName = if (fullUri.isNotEmpty()) {
                        Uri.parse(fullUri).lastPathSegment ?: ""
                    } else {
                        ""
                    }

                    val responseUpdatedClaim = postUpdateClaim(
                        context = context,
                        userId = userId,
                        indexUpdateClaim = claim.claimId,
                        updateClaimDescription = viewModel.description.value,
                        // pass the cleaned filename instead of the entire content://...
                        updateClaimPhoto = cleanedFileName,
                        updateClaimLocation = viewModel.location.value,
                        updateClaimStatus = viewModel.status.value
                    )

                    // Now upload the actual photo bits to the server, passing the same cleaned name
                    val responseUpdatePhoto = postMethodUploadPhoto(
                        context = context,
                        userId = userId,
                        claimId = claim.claimId,
                        fileName = cleanedFileName,
                        // BUT use the original full URI to read the actual bytes
                        imageUri = Uri.parse(fullUri)
                    )

                    if (responseUpdatedClaim != null && responseUpdatePhoto != null) {
                        // Update the local DataStore
                        val updatedClaim = ClaimInformation(
                            claimId = claim.claimId,
                            claimDes = viewModel.description.value,
                            claimPhoto = cleanedFileName, // store only the shortened name
                            claimLocation = viewModel.location.value,
                            claimStatus = viewModel.status.value
                        )

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
                        Toast.makeText(context, "Failed to update claim", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Update Claim")
            }
        }
    }
}

/**
 * Helper function to download a base64 image from server, decode it, and return an ImageBitmap.
 */
suspend fun generateClaimBitmap(fileName: String, context: Context): ImageBitmap? {
    val base64String = getMethodDownloadPhoto(context, fileName)
    if (base64String != null) {
        val imageBytes = Base64.decode(base64String, Base64.NO_WRAP or Base64.URL_SAFE)
        val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return bitmap?.asImageBitmap()
    }
    return null
}

/**
 * Displays the claim image by downloading & decoding it into an ImageBitmap.
 */
@Composable
fun DisplayClaimImage(fileName: String, context: Context) {
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }

    // This effect fetches/decodes the image once, whenever fileName changes
    LaunchedEffect(fileName) {
        val bitmap = generateClaimBitmap(fileName, context)
        imageBitmap.value = bitmap
    }

    if (imageBitmap.value != null) {
        Image(
            bitmap = imageBitmap.value!!,
            contentDescription = "Claim Photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Text("Loading image...")
        }
    }
}
