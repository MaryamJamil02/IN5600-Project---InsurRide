package com.example.in5600_project.presentation.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun GoBackButton(navController: NavController, onReset: () -> Unit = {}) {
    IconButton(onClick = {
        onReset()
        navController.popBackStack()


    }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Go Back"
        )
    }
}
