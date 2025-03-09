package com.example.in5600_project.presentation.ui.components

import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.in5600_project.data.datastore.UserManager
import com.example.in5600_project.data.network.methodPostRemoteLogin
import com.example.in5600_project.utils.hashPassword
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun LoginButton(modifier: Modifier, email: String, password: String) {
    val context = LocalContext.current
    val userManager = UserManager(context)
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {

            // Hash the password before sending it to the server
            val hashedPassword: String = hashPassword(password)
            println("Hashed Password: $hashedPassword")

            // Use a coroutine to call the server-side and local login method
            coroutineScope.launch {

                // Get the current list of the stored users
                val users = userManager.getUserPreferences().first()

                // Check if a user with the provided email is already logged in
                if (users.any { it.email == email && it.isLoggedIn }) {
                    Toast.makeText(context, "Already logged in", Toast.LENGTH_SHORT).show()
                }

                else {
                    // Continue with the network login call
                    val response = methodPostRemoteLogin(context, email, hashedPassword)

                    if (response == "OK") {
                        // Save the user's data in DataStore
                        userManager.saveUserPreferences(email, hashedPassword, true)
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    }

                    else {
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    ) {
        Text("Login")
    }
}


