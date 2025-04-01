package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.in5600_project.data.datastore.ClaimInformation
import com.example.in5600_project.presentation.ui.components.ClaimCard

@Composable
fun ClaimsListScreen(
    claims: List<ClaimInformation>,
    onClaimClick: (ClaimInformation) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(claims) { claim ->
            ClaimCard(
                claim = claim,
                onClick = { onClaimClick(claim) }
            )
        }
    }
}
