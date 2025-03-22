package com.example.in5600_project.presentation.ui.components

import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.in5600_project.data.datastore.ClaimsManager
import com.example.in5600_project.data.datastore.ClaimInformation
import kotlinx.coroutines.launch
import postInsertNewClaim

@Composable
fun NewClaimButton(
    userId: String,
    claimIndex: String,
    newClaimDescription: String,
    newClaimPhoto: String,
    newClaimLocation: String,
    newClaimStatus: String
) {
    val context = LocalContext.current
    val claimsManager = ClaimsManager(context)
    val coroutineScope = rememberCoroutineScope()

    Button(onClick = {
        coroutineScope.launch {
            // Call the network method to insert a new claim on the server.
            val responseNewClaim = postInsertNewClaim(
                context,
                userId,
                claimIndex,
                newClaimDescription,
                newClaimPhoto,
                newClaimLocation,
                newClaimStatus
            )

            if (responseNewClaim != null) {
                // Convert claimIndex to an integer for updateClaimAtIndex.
                val index = claimIndex.toIntOrNull() ?: 0
                val newClaim = ClaimInformation(
                    claimId = claimIndex,
                    claimDes = newClaimDescription,
                    claimPhoto = newClaimPhoto,
                    claimLocation = newClaimLocation,
                    claimStatus = newClaimStatus
                )

                // Insert the claim locally.
                claimsManager.updateOrInsertClaimAtIndex(userId, index, newClaim, true)

                Toast.makeText(context, "New claim added successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to add new claim", Toast.LENGTH_SHORT).show()
            }
        }
    }) {
        Text("Add New Claim")
    }
}

