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
import androidx.compose.ui.graphics.Color
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
import com.example.in5600_project.presentation.viewmodel.ClaimInfoViewModel
import com.example.in5600_project.utils.isValidLatLon
import kotlinx.coroutines.launch

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

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onPhotoChanged(it.toString()) }
    }

    LaunchedEffect(claim.claimPhoto) {
        if (!viewModel.isEditMode.value && claim.claimPhoto.isNotEmpty()) {
            viewModel.fetchPhoto(context, claim.claimPhoto)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    GoBackButton(navController, isPopBackStack = false)
                },
                title = {
                    Text(
                        "Claim Information",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (!viewModel.isEditMode.value) {
                // —— VIEW MODE ——

                // Details card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                Icon(
                                    imageVector = Icons.Filled.Tag,
                                    contentDescription = null,
                                    tint = Color(0xFF6C63FF),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Claim ID: ${claim.claimId}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = null,
                                    tint = Color(0xFF6C63FF),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Status:", style = MaterialTheme.typography.bodyLarge)
                                Spacer(Modifier.width(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFFD3F8E2)
                                ) {
                                    Text(
                                        text = claim.claimStatus,
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        ),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Message,
                                    contentDescription = null,
                                    tint = Color(0xFF6C63FF),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Description: ${claim.claimDes}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                // Location card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                MapBox(
                                    parts[0].trim().toDouble(),
                                    parts[1].trim().toDouble(),
                                    interactive = false
                                )
                            } else {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "No location data",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }

                // Photo card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                            DisplayClaimImage(
                                claim.claimPhoto,
                                context,
                                Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }

                // Edit button
                item {
                    Button(
                        onClick = {
                            viewModel.enterEditMode(claim)
                            viewModel.fetchPhoto(context, claim.claimPhoto)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Edit Claim", fontSize = 16.sp, color = Color.White)
                    }
                }
            } else {
                // —— EDIT MODE ——

                // 1) Description
                item {
                    OutlinedTextField(
                        value = viewModel.description.value,
                        onValueChange = { viewModel.onDescriptionChanged(it) },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 2) Status
                item {
                    Box(Modifier.fillMaxWidth()) {
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

                // 3) Location card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                MapBox(
                                    initialLatitude = parts[0].trim().toDouble(),
                                    initialLongitude = parts[1].trim().toDouble(),
                                    interactive = true
                                ) { newLat, newLong ->
                                    viewModel.onLocationChanged("$newLat, $newLong")
                                }
                            } else {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "No location data",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }

                // 4) Photo card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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

                            if (viewModel.photo.value.isNotEmpty()) {
                                AsyncImage(
                                    model = viewModel.photo.value,
                                    contentDescription = "Selected Photo",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }

                            Button(
                                onClick = { launcher.launch("image/*") },
                                modifier.fillMaxWidth()
                            )

                            {
                                Text("Change Photo")
                            }
                        }
                    }
                }

                // 5) Update Claim
                item {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val fullUri = viewModel.photo.value
                                val cleanedFileName = fullUri.substringAfterLast('/')
                                val responseUpdatedClaim = postUpdateClaim(
                                    context, userId, claim.claimId,
                                    viewModel.description.value,
                                    cleanedFileName, viewModel.location.value,
                                    viewModel.status.value
                                )
                                val responseUpdatePhoto = postMethodUploadPhoto(
                                    context, userId, claim.claimId,
                                    cleanedFileName, Uri.parse(fullUri)
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
                                        userId, claim.claimId.toInt(),
                                        updatedClaim, false
                                    )
                                    Toast.makeText(
                                        context,
                                        "Claim updated successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate("claimsHomeScreen")
                                    viewModel.exitEditMode()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed to update claim",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update Claim")
                    }
                }
            }
        }
    }
}

suspend fun generateClaimBitmap(fileName: String, context: Context): ImageBitmap? {
    val base64String = getMethodDownloadPhoto(context, fileName) ?: return null
    val imageBytes = Base64.decode(base64String, Base64.NO_WRAP or Base64.URL_SAFE)
    return android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        ?.asImageBitmap()
}

@Composable
fun DisplayClaimImage(fileName: String, context: Context, modifier: Modifier) {
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(fileName) {
        bitmap = generateClaimBitmap(fileName, context)
    }
    if (bitmap != null) {
        Image(bitmap = bitmap!!, contentDescription = "Claim Photo", modifier = modifier)
    } else {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("Loading image...")
        }
    }
}
