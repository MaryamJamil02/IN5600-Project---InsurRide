package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.in5600_project.data.datastore.ClaimsManager
import com.example.in5600_project.presentation.ui.components.ClaimsListScreen


// Main screen composable that uses ClaimsManager to get claim info.
@Composable
fun ClaimCardScreen(modifier: Modifier = Modifier, userId: String) {

    val context = LocalContext.current
    val claimsManager = remember { ClaimsManager(context) }
    val claims by claimsManager.getUserClaims(userId).collectAsState(initial = emptyList())

    // Display the list of claim cards.
    ClaimsListScreen(
        claims = claims,
        onPhotoClick = { claim ->

        }
    )
}