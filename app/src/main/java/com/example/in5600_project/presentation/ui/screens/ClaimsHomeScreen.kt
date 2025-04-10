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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.ClaimInformation


@Composable
fun ClaimsHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    claims: List<ClaimInformation>
) {


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
            navController = navController
        )
    }
}
