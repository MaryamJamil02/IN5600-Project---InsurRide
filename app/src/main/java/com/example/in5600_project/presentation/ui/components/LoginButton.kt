package com.example.in5600_project.presentation.ui.components

import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.in5600_project.data.datastore.UserInformation
import com.example.in5600_project.data.datastore.UserManager
import com.example.in5600_project.data.network.methodPostRemoteLogin
import com.example.in5600_project.utils.hashPassword
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.*

@Composable
fun LoginButton(modifier: Modifier, email: String, password: String) {
    val context = LocalContext.current
    val userManager = UserManager(context)
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            val hashedPassword: String = hashPassword(password)
            println("Hashed Password: $hashedPassword")


            coroutineScope.launch {
                // Assume methodPostRemoteLogin is a synchronous call.
                val response = methodPostRemoteLogin(context, email, hashedPassword)

                if (response == "OK") {
                    userManager.saveUserPreferences(email, hashedPassword, true)
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }

            }
            println("medina $userManager.getUserPreferences()")


        })
    {
        Text("Login")
    }
}



