package com.example.in5600_project.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.UserManager
import com.example.in5600_project.data.datastore.clearDataStore
import com.example.in5600_project.navigation.AppBottomBar
import com.example.in5600_project.presentation.ui.components.MapBox
import com.example.in5600_project.presentation.viewmodel.MyProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MyProfileScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: MyProfileViewModel) {

    val context = LocalContext.current
    val userManager = UserManager(context)
    // Collect the user id from the StateFlow
    val userId by viewModel.currentUserId.collectAsState()
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->
        // Home screen content goes here
        Column(modifier = modifier.padding(innerPadding)) {
            Text("Welcome to My Profile")



            Button(
                onClick = {
                    coroutineScope.launch {
                       clearDataStore(context)
                        withContext(Dispatchers.Main) {
                            navController.navigate("loginScreen")
                        }
                    }
                }
            ) {
                Text("Logout")
            }


            Button(
                onClick = { navController.navigate("changePasswordScreen")},
                modifier = modifier,
            ) {
                Text("Change Password")
            }
        }
    }
}


