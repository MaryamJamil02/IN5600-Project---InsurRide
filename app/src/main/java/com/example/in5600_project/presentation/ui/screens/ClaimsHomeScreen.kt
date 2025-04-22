package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.ClaimInformation
import com.example.in5600_project.navigation.AppBottomBar

@Composable
fun ClaimsHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    claims: List<ClaimInformation>
) {
    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally  // center children
        ) {
            Text(
                text = "My Claims",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )



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
                claims = claims,
                navController = navController
            )
        }
    }
}
