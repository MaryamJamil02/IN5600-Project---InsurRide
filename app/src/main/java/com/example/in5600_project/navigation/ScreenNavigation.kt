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
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.in5600_project.presentation.viewmodel.MyProfileViewModel


@Composable
fun MultipleScreenNavigator(modifier: Modifier, packageManager: PackageManager) {
    val navController = rememberNavController()

    // Create one instance of the ViewModel at this higher level
    val myProfileViewModel: MyProfileViewModel = viewModel()

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
                myProfileViewModel = myProfileViewModel
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


