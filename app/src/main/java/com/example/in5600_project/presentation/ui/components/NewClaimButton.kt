package com.example.in5600_project.presentation.ui.components

import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.ClaimsManager
import com.example.in5600_project.data.datastore.ClaimInformation
import kotlinx.coroutines.launch
import com.example.in5600_project.data.network.postInsertNewClaim
import com.example.in5600_project.data.network.postMethodUploadPhoto
import com.example.in5600_project.presentation.viewmodel.NewClaimViewModel
import kotlinx.coroutines.flow.first

@Composable
fun NewClaimButton(
    userId: String,
    newClaimDescription: String,
    newClaimPhoto: String,
    newClaimLocation: String,
    newClaimStatus: String,
    imageUri: Uri,
    navController: NavController,
    viewModel: NewClaimViewModel,
    modifier: Modifier

) {
    val context = LocalContext.current
    val claimsManager = ClaimsManager(context)
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            coroutineScope.launch {
                val numberOfClaims = claimsManager.getNumberOfClaims(userId).first()

                // Check if there are available slots for new claims
                if (numberOfClaims >= 5) {
                    Toast.makeText(context, "No available slot for new claim", Toast.LENGTH_SHORT)
                        .show()
                    return@launch
                } else {

                    // Call the network method to insert a new claim on the server
                    val responseNewClaim = postInsertNewClaim(
                        context,
                        userId,
                        numberOfClaims.toString(),
                        newClaimDescription,
                        newClaimPhoto,
                        newClaimLocation,
                        newClaimStatus
                    )

                    // Upload the photo to the server
                    val responseUploadPhoto = postMethodUploadPhoto(
                        context, userId, numberOfClaims.toString(), newClaimPhoto, imageUri
                    )

                    // Check if both operations were successful
                    if (responseNewClaim != null && responseUploadPhoto != null) {

                        // Create a new ClaimInformation object
                        val newClaim = ClaimInformation(
                            claimId = numberOfClaims.toString(),
                            claimDes = newClaimDescription,
                            claimPhoto = newClaimPhoto,
                            claimLocation = newClaimLocation,
                            claimStatus = newClaimStatus
                        )

                        // Insert the new claim locally
                        claimsManager.updateOrInsertClaimAtIndex(
                            userId, numberOfClaims, newClaim, true
                        )

                        Toast.makeText(context, "New claim added successfully", Toast.LENGTH_SHORT)
                            .show()

                        navController.navigate("claimInfoScreen/${numberOfClaims}")
                        viewModel.reset()

                    } else {
                        Toast.makeText(context, "Failed to add new claim", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }, modifier = modifier
    ) {
        Text("Add New Claim")
    }
}

