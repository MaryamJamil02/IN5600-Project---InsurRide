package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.ClaimsManager
import com.example.in5600_project.presentation.ui.screens.ClaimsListScreen

@Composable
fun ClaimCardScreen(
    modifier: Modifier = Modifier,
    userId: String,
    navController: NavController
) {
    val context = LocalContext.current
    val claimsManager = remember { ClaimsManager(context) }

    // Collect claims state from ClaimsManager using getUserClaims function.
    val claims by claimsManager.getUserClaims(userId).collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { navController.navigate("newClaimScreen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Claim")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Use the updated ClaimsListScreen with onClaimClick callback.
        ClaimsListScreen(
            claims = claims,
            onClaimClick = { claim ->
                // Navigate to the claim info screen.
                // Here we pass the claim id (or you could pass more info if using a Parcelable).
                navController.navigate("claimInfoScreen/${claim.claimId}")
            }
        )
    }
}
