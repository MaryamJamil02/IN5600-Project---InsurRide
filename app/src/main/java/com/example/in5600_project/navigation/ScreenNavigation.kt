package com.example.in5600_project.navigation

import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.in5600_project.presentation.ui.screens.ClaimsHomeScreen
import com.example.in5600_project.presentation.ui.screens.LoginScreen
import com.example.in5600_project.presentation.ui.screens.MyProfileScreen
import com.example.in5600_project.presentation.ui.screens.ChangePasswordScreen
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.in5600_project.data.datastore.ClaimInformation
import com.example.in5600_project.data.datastore.ClaimsManager
import com.example.in5600_project.presentation.ui.screens.ClaimInfoScreen
import com.example.in5600_project.presentation.ui.screens.NewClaimScreen
import com.example.in5600_project.presentation.viewmodel.ClaimInfoViewModel
import com.example.in5600_project.presentation.viewmodel.NewClaimViewModel
import com.example.in5600_project.presentation.viewmodel.MyProfileViewModel


@Composable
fun MultipleScreenNavigator(modifier: Modifier, packageManager: PackageManager) {
    val navController = rememberNavController()

    // Create one instance of the ViewModel at this higher level
    val myProfileViewModel: MyProfileViewModel = viewModel()
    val claimViewModel: NewClaimViewModel = viewModel()
    val context = LocalContext.current
    val claimsManager = remember { ClaimsManager(context) }
    val userId by myProfileViewModel.currentUserId.collectAsState()

    // Collect claims state from ClaimsManager using getUserClaims function.
    val claims by claimsManager.getUserClaims(userId).collectAsState(initial = emptyList())

    NavHost(navController = navController, startDestination = "loginScreen") {
        composable("loginScreen") {
            LoginScreen(
                modifier = modifier,
                navController = navController,
                myProfileViewModel = myProfileViewModel
            )
        }
        composable("claimsHomeScreen") {
            ClaimsHomeScreen(
                modifier = modifier,
                navController = navController,
                claims = claims
            )
        }
        composable("myProfileScreen") {
            MyProfileScreen(
                modifier = modifier,
                navController = navController,
                viewModel = myProfileViewModel
            )
        }

        composable("changePasswordScreen"){
            ChangePasswordScreen(
                modifier = modifier,
                navController = navController,
                viewModel = myProfileViewModel
            )
        }

        composable("newClaimScreen"){
            NewClaimScreen(
                modifier = modifier,
                navController = navController,
                claimViewModel = claimViewModel,
                myProfileViewModel = myProfileViewModel
            )
        }

        composable("claimInfoScreen/{claimId}") { backStackEntry ->
            val claimId = backStackEntry.arguments?.getString("claimId") ?: ""

            val claimInfoViewModel: ClaimInfoViewModel = viewModel()

            // Find the claim in the list by matching claimId.
            val claim: ClaimInformation = claims.find { it.claimId == claimId }
                ?: ClaimInformation("", "", "", "", "")
            ClaimInfoScreen(
                modifier = modifier,
                claim = claim,
                navController = navController,
                viewModel = claimInfoViewModel,
                context = context,
                userId = userId
            )
        }
    }
}

@Composable
fun AppBottomBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate("claimsHomeScreen") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "My Profile") },
            label = { Text("My Profile") },
            selected = false,
            onClick = { navController.navigate("myProfileScreen") }
        )
    }
}


