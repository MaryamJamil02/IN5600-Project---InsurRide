package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MapScreen(modifier: Modifier) {
    Box(modifier = modifier.background(Color.Blue).fillMaxSize()) {
        Text("Welcome to Maps")
    }
}