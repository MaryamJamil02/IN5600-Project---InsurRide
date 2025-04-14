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
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.in5600_project.presentation.ui.components.MapBox
import com.example.in5600_project.utils.isValidLatLon
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
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
    val coordinates = claim.claimLocation

    // Launcher to pick an image from the gallery (only used in edit mode).
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
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
        // VIEW MODE (using a LazyColumn)
        // --------------------------
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text("Claim Information", modifier = Modifier.padding(16.dp))
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                GoBackButton(navController, isPopBackStack = false)
            }
            item {
                Spacer(modifier = Modifier.height(15.dp))
            }
            item {
                Text(
                    text = "Claim ID: ${claim.claimId}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Text(
                    text = "Status: ${claim.claimStatus}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
            item {
                Text(
                    text = "Description: ${claim.claimDes}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
            item {
                // Fixed height for the map preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    if (isValidLatLon(coordinates)) {
                        val parts: List<String> = coordinates.split(",")
                        val lat = parts[0].trim().toDouble()
                        val long = parts[1].trim().toDouble()

                        // Non-interactive map
                        MapBox(lat, long, false)
                    } else {
                        Text("Invalid coordinates")
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                // Show the claim image
                DisplayClaimImage(claim.claimPhoto, context)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Button(
                    onClick = {
                        viewModel.enterEditMode(claim)
                        viewModel.fetchPhoto(context, claim.claimPhoto)
                    }
                ) {
                    Text("Edit Claim")
                }
            }
        }
    } else {
        // --------------------------
        // EDIT MODE (using a LazyColumn)
        // --------------------------
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text("Edit Claim Information", modifier = Modifier.padding(16.dp))
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                GoBackButton(
                    navController,
                    onReset = { viewModel.exitEditMode() },
                    isPopBackStack = false
                )
            }
            item {
                Spacer(modifier = Modifier.height(15.dp))
            }
            item {
                OutlinedTextField(
                    value = viewModel.description.value,
                    onValueChange = { viewModel.onDescriptionChanged(it) },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                // Editable map
                if (isValidLatLon(coordinates)) {
                    val parts: List<String> = coordinates.split(",")
                    // Use either the old or newly updated location from the ViewModel
                    val lat = parts[0].trim().toDouble()
                    val long = parts[1].trim().toDouble()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        // Pass a callback to update the location in the ViewModel when the user taps the map
                        MapBox(
                            latitude = lat,
                            longitude = long,
                            interactive = true
                        ) { newLat, newLong ->
                            // Whenever the user taps, store these new coords
                            viewModel.onLocationChanged("$newLat, $newLong")
                        }
                    }
                } else {
                    Text("Invalid coordinates")
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                // Dropdown for Status
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = viewModel.status.value,
                        onValueChange = {},
                        label = { Text("Status") },
                        modifier = Modifier.fillMaxWidth(),
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
                        modifier = Modifier.fillMaxWidth()
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
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                // Button to select a new photo from the gallery
                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Change Photo")
                }
            }
            item {
                // Preview the newly selected image, if any
                if (viewModel.photo.value.isNotEmpty()) {
                    AsyncImage(
                        model = viewModel.photo.value,
                        contentDescription = "Selected Photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                // Button to confirm the update
                Button(onClick = {
                    coroutineScope.launch {
                        val fullUri = viewModel.photo.value
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
                            updateClaimPhoto = cleanedFileName,
                            updateClaimLocation = viewModel.location.value,
                            updateClaimStatus = viewModel.status.value
                        )

                        val responseUpdatePhoto = postMethodUploadPhoto(
                            context = context,
                            userId = userId,
                            claimId = claim.claimId,
                            fileName = cleanedFileName,
                            imageUri = Uri.parse(fullUri)
                        )

                        if (responseUpdatedClaim != null && responseUpdatePhoto != null) {
                            val updatedClaim = ClaimInformation(
                                claimId = claim.claimId,
                                claimDes = viewModel.description.value,
                                claimPhoto = cleanedFileName,
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
