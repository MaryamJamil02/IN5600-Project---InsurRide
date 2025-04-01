package com.example.in5600_project.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.in5600_project.data.datastore.ClaimInformation
import coil.compose.AsyncImage

@Composable
fun ClaimCard(
    claim: ClaimInformation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Show the claim photo (make sure your project has the Coil dependency).
            AsyncImage(
                model = claim.claimPhoto,
                contentDescription = "Claim Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Claim ID: ${claim.claimId}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Status: ${claim.claimStatus}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
