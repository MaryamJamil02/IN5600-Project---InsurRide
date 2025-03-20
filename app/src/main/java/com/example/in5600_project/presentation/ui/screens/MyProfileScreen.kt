package com.example.in5600_project.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.UserManager
import com.example.in5600_project.navigation.*
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

    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->
        // Home screen content goes here
        Column(modifier = modifier.padding(innerPadding)) {
            Text("Welcome to My Profile")
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        // Logout the user by setting isLoggedIn to false in DataStore
                        userManager.logoutUser(userId)

                        // Retrieve the updated user preferences
                        val users = userManager.getUserPreferences().first()
                        val currentUser = users.find { it.id == userId }

                        // Check if the user's isLoggedIn flag is now false
                        if (currentUser == null || !currentUser.isLoggedIn) {
                            // Switch to the Main thread to show the toast
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Logout successful for $userId", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Logout failed", Toast.LENGTH_SHORT).show()
                            }
                        }

                        // Navigate to the login screen after the check
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