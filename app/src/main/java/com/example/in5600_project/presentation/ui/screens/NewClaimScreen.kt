package com.example.in5600_project.presentation.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.in5600_project.presentation.ui.components.GoBackButton
import com.example.in5600_project.presentation.ui.components.MapBox
import com.example.in5600_project.presentation.ui.components.NewClaimButton
import com.example.in5600_project.presentation.viewmodel.NewClaimViewModel
import com.example.in5600_project.presentation.viewmodel.MyProfileViewModel
import com.example.in5600_project.utils.isValidLatLon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewClaimScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    claimViewModel: NewClaimViewModel = viewModel(),
    myProfileViewModel: MyProfileViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val userId by myProfileViewModel.currentUserId.collectAsState()
    val context = LocalContext.current

    val description = claimViewModel.description.value
    val location = claimViewModel.location.value
    val selectedStatus = claimViewModel.selectedStatus.value
    val statusOptions = claimViewModel.statusOptions
    val imageUri = claimViewModel.imageUri.value

    // Launcher for photo selection
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> claimViewModel.onImageUriChanged(uri) }

    // Parse location into lat/lon
    val (parsedLat, parsedLong) = remember(location) {
        if (isValidLatLon(location)) {
            val parts = location.split(",")
            parts[0].trim().toDouble() to parts[1].trim().toDouble()
        } else null to null
    }

    // Clean file name
    val cleanedFileName = imageUri?.lastPathSegment?.substringBeforeLast(".") ?: ""

    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            GoBackButton(navController, onReset = { claimViewModel.reset() })
        }, title = {
            Text(
                "Add New Claim", fontSize = 22.sp, fontWeight = FontWeight.Bold
            )
        })
    }) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Description field
            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { claimViewModel.onDescriptionChanged(it) },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            // Status dropdown
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = selectedStatus,
                        onValueChange = {},
                        label = { Text("Status") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = "Select Status"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        statusOptions.forEach { status ->
                            DropdownMenuItem(text = { Text(status) }, onClick = {
                                claimViewModel.onStatusSelected(status)
                                expanded = false
                            })
                        }
                    }
                }
            }

            // Photo section
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Photo", style = MaterialTheme.typography.titleMedium)

                        imageUri?.let { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(model = uri),
                                contentDescription = "Selected Photo",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }

                        Button(
                            onClick = { launcher.launch("image/*") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Select Photo")
                        }
                    }
                }
            }

            // Location section
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Location", style = MaterialTheme.typography.titleMedium)
                        MapBox(
                            initialLatitude = parsedLat,
                            initialLongitude = parsedLong,
                            interactive = true
                        ) { lat, lon ->
                            claimViewModel.onLocationChanged("$lat, $lon")
                        }
                    }
                }
            }

            // Create Claim
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    if (description.isNotBlank() && location.isNotBlank() && imageUri != null) {

                        // New Claim Button
                        NewClaimButton(
                            userId = userId,
                            newClaimDescription = description,
                            newClaimPhoto = cleanedFileName,
                            newClaimLocation = location,
                            newClaimStatus = selectedStatus,
                            imageUri = imageUri,
                            navController = navController,
                            viewModel = claimViewModel,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Button(
                            onClick = {
                                when {
                                    description.isBlank() -> Toast.makeText(
                                        context, "Please enter a description", Toast.LENGTH_SHORT
                                    ).show()

                                    imageUri == null -> Toast.makeText(
                                        context, "Please select a photo", Toast.LENGTH_SHORT
                                    ).show()

                                    location.isBlank() -> Toast.makeText(
                                        context, "Please select a location", Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }, modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add New Claim")
                        }
                    }
                }
            }
        }
    }
}
