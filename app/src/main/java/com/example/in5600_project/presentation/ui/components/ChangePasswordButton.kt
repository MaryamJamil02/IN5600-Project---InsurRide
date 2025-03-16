package com.example.in5600_project.presentation.ui.components

import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.in5600_project.data.datastore.UserManager
import com.example.in5600_project.data.network.methodPostChangePasswd
import com.example.in5600_project.utils.hashPassword
import kotlinx.coroutines.launch


@Composable
fun ChangePasswordButton(email: String, clearPassword: String) {

    val context = LocalContext.current
    val userManager = UserManager(context)
    val coroutineScope = rememberCoroutineScope()

    Button(onClick = {

        // Hash the password before sending it to the server
        val hashedPassword: String = hashPassword(clearPassword)

        println("Hashed Password: $hashedPassword")


        coroutineScope.launch {

            // Send the password change request to the server
            val response = methodPostChangePasswd(context, email, clearPassword, hashedPassword)

            if (response == "OK") {
                // Change the user's password in DataStore
                userManager.changeUserPassword(email, hashedPassword)
                Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Password change failed", Toast.LENGTH_SHORT).show()
            }


        }
    }) {
        Text("Change Password")
    }
}



