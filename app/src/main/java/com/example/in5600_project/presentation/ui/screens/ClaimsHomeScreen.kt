package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.in5600_project.navigation.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.in5600_project.presentation.viewmodel.MyProfileViewModel

@Composable
fun ClaimsHomeScreen(modifier: Modifier = Modifier, navController: NavController, myProfileViewModel: MyProfileViewModel) {
    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            Text("Welcome to Home")
        }
        SwipeScreens(modifier, innerPadding, myProfileViewModel)
    }
}

@Composable
fun SwipeScreens(modifier: Modifier, padding: PaddingValues, myProfileViewModel: MyProfileViewModel) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val userId by myProfileViewModel.currentUserId.collectAsState()


    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> {
                // Map screen
                MapScreen(modifier = modifier)
            }
            1 -> {
                // Cards screen
                ClaimCardScreen(modifier = modifier, userId = userId)
            }
        }
    }
}