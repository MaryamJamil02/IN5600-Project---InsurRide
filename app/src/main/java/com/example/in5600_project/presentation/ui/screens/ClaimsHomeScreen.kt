package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.ClaimInformation
import com.example.in5600_project.navigation.AppBottomBar
import com.example.in5600_project.presentation.ui.theme.PrimaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaimsHomeScreen(
    modifier: Modifier = Modifier, navController: NavController, claims: List<ClaimInformation>
) {
    Scaffold(bottomBar = { AppBottomBar(navController) }, topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "My Claims", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = PrimaryDark, titleContentColor = Color.White
            )
        )
    }) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Add New Claim Button
            IconButton(
                onClick = { navController.navigate("newClaimScreen") },
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(48.dp)
                    .align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Add New Claim",
                    tint = Color(0xFF213555),
                    modifier = Modifier.size(60.dp)
                )
            }

            ClaimsListScreen(
                claims = claims, navController = navController
            )
        }
    }
}
