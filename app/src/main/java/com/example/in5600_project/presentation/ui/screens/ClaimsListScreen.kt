package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.ClaimInformation
import com.example.in5600_project.presentation.ui.components.ClaimCard

@Composable
fun ClaimsListScreen(
    claims: List<ClaimInformation>, navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(claims) { index, claim ->
            ClaimCard(
                displayNumber = index + 1, claim = claim, navController = navController
            )
        }
    }
}
