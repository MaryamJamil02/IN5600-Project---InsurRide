package com.example.in5600_project.presentation.ui.screens

//LATERFIX - use * instead
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.in5600_project.presentation.ui.components.NewClaimButton
import com.example.in5600_project.presentation.viewmodel.ClaimViewModel
import com.example.in5600_project.presentation.viewmodel.MyProfileViewModel

@Composable
fun NewClaimScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    claimViewModel: ClaimViewModel = viewModel(),
    myProfileViewModel: MyProfileViewModel,
) {
    var expanded by remember { mutableStateOf(false) }
    val userId by myProfileViewModel.currentUserId.collectAsState()


    val description = claimViewModel.description.value
    val location = claimViewModel.location.value
    val selectedStatus = claimViewModel.selectedStatus.value
    val statusOptions = claimViewModel.statusOptions
    val imageUri = claimViewModel.imageUri.value


    // Launcher to pick an image from the gallery
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        claimViewModel.onImageUriChanged(uri)
    }

    // Extract the filename from imageUri
    val imageFileName = imageUri?.lastPathSegment ?: ""
    println("Selected image filename: $imageFileName")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Description.
        OutlinedTextField(
            value = description,
            onValueChange = { claimViewModel.onDescriptionChanged(it) },
            label = { Text("Description") },
            modifier = modifier.fillMaxWidth()
        )

        // Location
        OutlinedTextField(
            value = location,
            onValueChange = { claimViewModel.onLocationChanged(it) },
            label = { Text("Location") },
            modifier = modifier.fillMaxWidth()
        )

        // Dropdown for Status
        Box(modifier = modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedStatus,
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

        // Button to select a photo from the gallery
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = modifier.fillMaxWidth()
        ) {
            Text("Select Photo")
        }

        // Display a preview of the selected image, if it's available
        imageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = "Selected Photo",
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        // Pass the image filename to the NewClaimButton.
        if (imageUri != null) {
            NewClaimButton(
                userId = userId,
                newClaimDescription = description,
                newClaimPhoto = imageFileName,
                newClaimLocation = location,
                newClaimStatus = selectedStatus,
                imageUri = imageUri
            )
        }
    }
}
