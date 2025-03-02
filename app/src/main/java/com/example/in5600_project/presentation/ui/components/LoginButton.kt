package com.example.in5600_project.presentation.ui.components

import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.in5600_project.data.network.makeGetRequest
import com.example.in5600_project.data.network.methodPostRemoteLogin
import com.example.in5600_project.utils.hashPassword


@Composable
fun LoginButton(modifier: Modifier, email: String, password: String) {

    val context = LocalContext.current
    Button(
        onClick = {
            val hashedPassword: String = hashPassword(password)
            println("Hashed Password: $hashedPassword")

            methodPostRemoteLogin(context, email, hashedPassword)
            Toast.makeText(context, "GET REQUEST", Toast.LENGTH_SHORT).show()
        },
        modifier = modifier
    ) {
        Text("GET")
    }
}
