package com.example.in5600_project.presentation.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.in5600_project.data.datastore.ClaimInformation
import com.example.in5600_project.data.datastore.ClaimsManager
import com.example.in5600_project.data.network.getMethodDownloadPhoto
import com.example.in5600_project.data.network.postMethodUploadPhoto
import com.example.in5600_project.data.network.postUpdateClaim
import com.example.in5600_project.presentation.ui.components.GoBackButton
import com.example.in5600_project.presentation.ui.components.MapBox
import com.example.in5600_project.presentation.ui.components.StatusBadge
import com.example.in5600_project.presentation.viewmodel.ClaimInfoViewModel
import com.example.in5600_project.utils.isValidLatLon
import kotlinx.coroutines.launch
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import java.io.File
import java.io.FileOutputStream

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
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

    // Launch an activity to get an image from the gallery
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onPhotoChanged(it.toString()) }
    }

    // Fetch the photo from the server and update the photo state
    LaunchedEffect(claim.claimPhoto) {
        if (!viewModel.isEditMode.value && claim.claimPhoto.isNotEmpty()) {
            viewModel.fetchPhoto(context, claim.claimPhoto)
        }
    }

    Scaffold(topBar = {
        TopAppBar(navigationIcon = { GoBackButton(navController) }, title = {
            Text(
                "Claim Information", fontSize = 22.sp, fontWeight = FontWeight.Bold
            )
        })
    }) { innerPadding ->

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // —— VIEW MODE ——
            if (!viewModel.isEditMode.value) {

                // Edit button
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.enterEditMode(claim)
                                viewModel.fetchPhoto(context, claim.claimPhoto)
                            }, Modifier.background(
                                color = Color(0xFFE3F2FD), shape = RoundedCornerShape(50)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Claim",
                                tint = Color(0xFF213555)
                            )
                        }
                    }
                }

                // Claim information
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                // Claim ID
                                Icon(
                                    imageVector = Icons.Filled.Tag,
                                    contentDescription = null,
                                    tint = Color(0xFF213555),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Claim ID: ${claim.claimId}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                // Claim status
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = null,
                                    tint = Color(0xFF213555),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Status:",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.width(4.dp))
                                StatusBadge(status = claim.claimStatus)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                // Claim description
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Message,
                                    contentDescription = null,
                                    tint = Color(0xFF213555),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Description: ${claim.claimDes}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                item {

                    // Claim location
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Location", style = MaterialTheme.typography.titleMedium)
                            if (isValidLatLon(coordinates)) {
                                val parts = coordinates.split(",")

                                // Display the map with the claim location
                                MapBox(
                                    parts[0].trim().toDouble(),
                                    parts[1].trim().toDouble(),
                                    interactive = false
                                )
                            } else {

                                // Display the map with no claim location
                                MapBox(
                                    initialLatitude = null,
                                    initialLongitude = null,
                                    interactive = false
                                )
                            }
                        }
                    }
                }

                item {

                    // Claim photo
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Photo", style = MaterialTheme.typography.titleMedium)

                            // Display the claim photo
                            DisplayClaimImage(
                                fileName = claim.claimPhoto,
                                context = context,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }

            } else {
                // —— EDIT MODE ——

                // Claim information
                item {

                    // Claim description
                    OutlinedTextField(
                        value = viewModel.description.value,
                        onValueChange = { viewModel.onDescriptionChanged(it) },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }

                item {

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {

                        // Claim status
                        OutlinedTextField(
                            value = viewModel.status.value,
                            onValueChange = {},
                            label = { Text("Status") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            viewModel.statusOptions.forEach { status ->
                                DropdownMenuItem(text = { Text(status) }, onClick = {
                                    viewModel.onStatusChanged(status)
                                    expanded = false
                                })
                            }
                        }
                    }
                }

                item {

                    // Claim location
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Location", style = MaterialTheme.typography.titleMedium)
                            if (isValidLatLon(coordinates)) {
                                val parts = coordinates.split(",")

                                // Display the map with the claim location
                                MapBox(
                                    initialLatitude = parts[0].trim().toDouble(),
                                    initialLongitude = parts[1].trim().toDouble(),
                                    interactive = true
                                ) { newLat, newLong ->
                                    viewModel.onLocationChanged("$newLat, $newLong")
                                }
                            } else {

                                // Display the map with no claim location
                                MapBox(
                                    initialLatitude = null,
                                    initialLongitude = null,
                                    interactive = true
                                ) { newLat, newLong ->
                                    viewModel.onLocationChanged("$newLat, $newLong")
                                }
                            }
                        }
                    }
                }

                item {

                    // Claim photo
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Photo", style = MaterialTheme.typography.titleMedium)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) {
                                IconButton(
                                    onClick = { launcher.launch("image/*") },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .align(Alignment.TopEnd)
                                        .offset(y = (-20).dp)
                                        .background(Color(0xFFF7FCFD), CircleShape)
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Change photo",
                                        modifier = Modifier.size(24.dp),
                                        tint = Color(0xFF213555)
                                    )
                                }

                                // Display the claim photo
                                if (viewModel.photo.value.isNotEmpty()) {
                                    AsyncImage(
                                        model = viewModel.photo.value,
                                        contentDescription = "Selected Photo",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .align(Alignment.BottomCenter)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                } else {

                                    // Display default image if no photo is selected
                                    DisplayClaimImage(
                                        fileName = claim.claimPhoto,
                                        context = context,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            coroutineScope.launch {

                                // Update the claim
                                val fullUri = viewModel.photo.value

                                // Extract the file name from the URI
                                val cleanedFileName = fullUri.substringAfterLast('/')

                                // Update the claim to the server
                                val responseUpdatedClaim = postUpdateClaim(
                                    context,
                                    userId,
                                    claim.claimId,
                                    viewModel.description.value,
                                    cleanedFileName,
                                    viewModel.location.value,
                                    viewModel.status.value
                                )

                                //  Upload the photo to the server
                                val responseUpdatePhoto = postMethodUploadPhoto(
                                    context,
                                    userId,
                                    claim.claimId,
                                    cleanedFileName,
                                    Uri.parse(fullUri)
                                )

                                // Check if the update was successful
                                if (responseUpdatedClaim != null && responseUpdatePhoto != null) {
                                    val updatedClaim = ClaimInformation(
                                        claimId = claim.claimId,
                                        claimDes = viewModel.description.value,
                                        claimPhoto = cleanedFileName,
                                        claimLocation = viewModel.location.value,
                                        claimStatus = viewModel.status.value
                                    )

                                    // Update the claim in the local datastore
                                    claimsManager.updateOrInsertClaimAtIndex(
                                        userId, claim.claimId.toInt(), updatedClaim, false
                                    )

                                    Toast.makeText(
                                        context, "Claim updated successfully", Toast.LENGTH_SHORT
                                    ).show()

                                    // Navigate back to the claim list screen and exit edit mode
                                    navController.popBackStack()
                                    viewModel.exitEditMode()

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Offline - failed to update claim",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Update Claim")
                    }
                }
            }
        }
    }
}

// Generate a bitmap from a file name
suspend fun generateClaimBitmap(fileName: String, context: Context): ImageBitmap? {

    val cacheFile = File(context.filesDir, fileName)
    if (cacheFile.exists()) {
        return BitmapFactory.decodeFile(cacheFile.absolutePath)?.asImageBitmap()
    }

    // Download the photo from the server
    val base64String = getMethodDownloadPhoto(context, fileName) ?: return null

    val imageBytes = Base64.decode(base64String, Base64.NO_WRAP or Base64.URL_SAFE)


    // Convert the image bytes to a bitmap
    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.let { bmp ->
        try {
            FileOutputStream(cacheFile).use { out ->
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()
}

// Display a claim image
@Composable
fun DisplayClaimImage(fileName: String, context: Context, modifier: Modifier) {

    // Generate the bitmap
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(fileName) {
        bitmap = generateClaimBitmap(fileName, context)
    }
    if (bitmap != null) {
        Image(bitmap = bitmap!!, contentDescription = "Claim Photo", modifier = modifier)
    } else {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("No image found...")
        }
    }
}
