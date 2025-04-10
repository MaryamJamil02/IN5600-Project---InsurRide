package com.example.in5600_project.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.in5600_project.data.datastore.ClaimInformation
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.in5600_project.presentation.ui.screens.DisplayClaimImage

@Composable
fun ClaimCard(
    claim: ClaimInformation,
    onClickPhoto: () -> Unit = {},
    navController: NavController
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { navController.navigate("claimInfoScreen/${claim.claimId}") }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Claim ID: ${claim.claimId}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Status: ${claim.claimStatus}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            DisplayClaimImage(claim.claimPhoto, context)


            /*Text(
                text = "Photo: ${claim.claimPhoto}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onClickPhoto() }
            )*/
        }
    }
}