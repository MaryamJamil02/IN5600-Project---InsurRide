package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.ClaimsManager


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

        // ClaimsListScreen to display the list of claims.
        ClaimsListScreen(
            claims = claims,
            onPhotoClick = { claim ->
                // Handle photo click if needed
            }
        )
    }
}
