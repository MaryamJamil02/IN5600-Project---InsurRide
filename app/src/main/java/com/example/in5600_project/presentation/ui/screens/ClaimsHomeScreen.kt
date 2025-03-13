package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.in5600_project.navigation.BottomBar
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun ClaimsHomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        // Home screen content goes here
        Box(modifier = modifier.padding(innerPadding)) {
            Text("Welcome to Home")
        }
        SwipeScreens(modifier, innerPadding)
    }
}

@Composable
fun SwipeScreens(modifier: Modifier, padding: PaddingValues) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> {
                // Compose your Map screen here.
                MapScreen(modifier = modifier)
            }
            1 -> {
                // Compose your Cards screen here.
                ClaimCardScreen(modifier = modifier)
            }
        }
    }
}