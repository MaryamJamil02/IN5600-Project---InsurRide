package com.example.in5600_project.presentation.ui.components

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.in5600_project.data.datastore.ClaimsManager
import com.example.in5600_project.data.datastore.ClaimInformation
import kotlinx.coroutines.launch
import com.example.in5600_project.data.network.postInsertNewClaim
import com.example.in5600_project.data.network.postMethodUploadPhoto
import kotlinx.coroutines.flow.first
import java.io.InputStream

@Composable
fun NewClaimButton(
    userId: String,
    newClaimDescription: String,
    newClaimPhoto: String,
    newClaimLocation: String,
    newClaimStatus: String,
    imageUri : Uri
) {
    val context = LocalContext.current
    val claimsManager = ClaimsManager(context)
    val coroutineScope = rememberCoroutineScope()

    Button(onClick = {
        coroutineScope.launch {
            val numberOfClaims = claimsManager.getNumberOfClaims(userId).first()

            println("Number of claims: $numberOfClaims")

            // Check if there are available slots for new claims
            if (numberOfClaims >= 5) {
                Toast.makeText(context, "No available slot for new claim", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            } else {

                // Call the network method to insert a new claim on the server.
                val responseNewClaim = postInsertNewClaim(
                    context,
                    userId,
                    numberOfClaims.toString(),
                    newClaimDescription,
                    newClaimPhoto,
                    newClaimLocation,
                    newClaimStatus
                )
                
                val responseUploadPhoto = postMethodUploadPhoto(
                    context,
                    userId,
                    numberOfClaims.toString(),
                    newClaimPhoto,
                    imageUri
                )
                    

                if (responseNewClaim != null) {
                    // Create a new ClaimInformation object.
                    val newClaim = ClaimInformation(
                        claimId = numberOfClaims.toString(),
                        claimDes = newClaimDescription,
                        claimPhoto = newClaimPhoto,
                        claimLocation = newClaimLocation,
                        claimStatus = newClaimStatus
                    )

                    // Insert the new claim locally.
                    claimsManager.updateOrInsertClaimAtIndex(userId, numberOfClaims, newClaim, true)

                    // LATERFIX - navigate claiminfoscreen

                    Toast.makeText(context, "New claim added successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Failed to add new claim", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }) {
        Text("Add New Claim")
    }
}


