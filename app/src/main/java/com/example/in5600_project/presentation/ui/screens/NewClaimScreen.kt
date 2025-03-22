package com.example.in5600_project.presentation.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.in5600_project.data.datastore.ClaimInformation
import com.example.in5600_project.presentation.ui.components.NewClaimButton
import com.example.in5600_project.presentation.viewmodel.ClaimViewModel

@Composable
fun NewClaimScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    claimViewModel: ClaimViewModel = viewModel()
) {
    var expanded by remember { mutableStateOf(false) }

    val description = claimViewModel.description.value
    val location = claimViewModel.location.value
    val selectedStatus = claimViewModel.selectedStatus.value
    val statusOptions = claimViewModel.statusOptions
    val imageUri = claimViewModel.imageUri.value

    // Launcher to pick an image from the gallery.
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        claimViewModel.onImageUriChanged(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // OutlinedTextField for Description.
        OutlinedTextField(
            value = description,
            onValueChange = { claimViewModel.onDescriptionChanged(it) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        // OutlinedTextField for Location.
        OutlinedTextField(
            value = location,
            onValueChange = { claimViewModel.onLocationChanged(it) },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        // Dropdown for Status selection.
        androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxWidth()) {
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
        // Button to select a photo from the gallery.
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Photo")
        }
        // Display a preview of the selected image, if available.
        imageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = "Selected Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        //NewClaimButton(userId, claimIndex, description, imageUri, location, selectedStatus)

    }


}
