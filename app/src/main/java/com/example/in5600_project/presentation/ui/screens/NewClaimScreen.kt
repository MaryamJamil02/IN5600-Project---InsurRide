package com.example.in5600_project.presentation.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.in5600_project.presentation.ui.components.GoBackButton
import com.example.in5600_project.presentation.ui.components.MapBox
import com.example.in5600_project.presentation.ui.components.NewClaimButton
import com.example.in5600_project.presentation.viewmodel.NewClaimViewModel
import com.example.in5600_project.presentation.viewmodel.MyProfileViewModel
import com.example.in5600_project.utils.isValidLatLon

@Composable
fun NewClaimScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    claimViewModel: NewClaimViewModel = viewModel(),
    myProfileViewModel: MyProfileViewModel,
) {
    var expanded by remember { mutableStateOf(false) }
    val userId by myProfileViewModel.currentUserId.collectAsState()
    val context = LocalContext.current

    val description = claimViewModel.description.value
    val location = claimViewModel.location.value
    val selectedStatus = claimViewModel.selectedStatus.value
    val statusOptions = claimViewModel.statusOptions
    val imageUri = claimViewModel.imageUri.value

    // Choose a photo from the gallery
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        claimViewModel.onImageUriChanged(uri)
    }

    // Parse the location text into lat/long if valid, otherwise null
    val (parsedLat, parsedLong) = remember(location) {
        if (isValidLatLon(location)) {
            val parts = location.split(",")
            val lat = parts[0].trim().toDouble()
            val lon = parts[1].trim().toDouble()
            lat to lon
        } else {
            // No pin initially
            null to null
        }
    }

    // For displaying the file name
    val cleanedFileName = imageUri?.lastPathSegment?.substringBeforeLast(".") ?: ""

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Title
        item {
            Text("Add New Claim", modifier = Modifier.padding(16.dp))
        }
        // 2. Spacer
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
        // 3. GoBackButton
        item {
            GoBackButton(navController, onReset = { claimViewModel.reset() }, isPopBackStack = false)
        }
        // 4. Spacer
        item {
            Spacer(modifier = Modifier.height(15.dp))
        }
        // 5. Description
        item {
            OutlinedTextField(
                value = description,
                onValueChange = { claimViewModel.onDescriptionChanged(it) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        // 6. Status dropdown
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedStatus,
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
                    statusOptions.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status) },
                            onClick = {
                                claimViewModel.onStatusSelected(status)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        // 7. Photo selection button
        item {
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Photo")
            }
        }
        // 8. Display the selected image, if any
        item {
            imageUri?.let { uri ->
                Column {
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = "Selected Photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Text("Filename: $cleanedFileName")
                }
            }
        }
        // 9. Map area
        item {

                MapBox(
                    initialLatitude = parsedLat,
                    initialLongitude = parsedLong,
                    interactive = true
                ) { lat, lon ->
                    // Update location in the ViewModel
                    claimViewModel.onLocationChanged("$lat, $lon")
                }

        }
        // 10. "Create Claim" button (only if an image is selected)
        item {
            if (imageUri != null && cleanedFileName.isNotEmpty()) {
                NewClaimButton(
                    userId = userId,
                    newClaimDescription = description,
                    newClaimPhoto = cleanedFileName,
                    newClaimLocation = location,
                    newClaimStatus = selectedStatus,
                    imageUri = imageUri,
                    navController = navController,
                    viewModel = claimViewModel
                )
            }
        }
    }
}
