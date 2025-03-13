package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun ClaimCardScreen(modifier: Modifier) {

    Box(modifier = modifier.background(Color.Red).fillMaxSize()) {
        Text("Welcome to Cards")
    }

}
