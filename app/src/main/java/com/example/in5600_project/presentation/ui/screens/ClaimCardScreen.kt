package com.example.in5600_project.presentation.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.ClaimsManager


// Main screen composable that uses ClaimsManager to get claim info.
@Composable
fun ClaimCardScreen(modifier: Modifier = Modifier, userId: String, navController: NavController) {

    val context = LocalContext.current
    val claimsManager = remember { ClaimsManager(context) }
    val claims by claimsManager.getUserClaims(userId).collectAsState(initial = emptyList())

    // Display the list of claim cards.
    Button(onClick = {navController.navigate("newClaimScreen")}) {
        Text("Add New Claim")
    }
    ClaimsListScreen(
        claims = claims,
        onPhotoClick = { claim ->

        }
    )
}