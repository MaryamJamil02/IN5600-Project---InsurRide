package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.in5600_project.navigation.BottomBar

@Composable
fun ClaimsHomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        // Home screen content goes here
        Box(modifier = modifier.padding(innerPadding)) {
            Text("Welcome to Home")
        }
    }
}