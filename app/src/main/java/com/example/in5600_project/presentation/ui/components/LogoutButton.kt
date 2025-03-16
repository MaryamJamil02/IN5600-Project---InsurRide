package com.example.in5600_project.presentation.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LogoutButton(modifier: Modifier, email: String, navController: NavController, coroutineScope: CoroutineScope) {
    val userManager = UserManager(LocalContext.current)

    Button(
        onClick = {
            coroutineScope.launch {
                // Log out the user
                userManager.logoutUser(email)
                navController.navigate("login") // Navigate to login screen after logout
            }
        },
        modifier = modifier
    ) {
        Text("Logout")
    }
}
