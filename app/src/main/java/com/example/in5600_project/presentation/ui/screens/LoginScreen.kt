package com.example.in5600_project.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.in5600_project.data.datastore.clearDataStore
import com.example.in5600_project.presentation.ui.components.LoginButton
import com.example.in5600_project.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(modifier: Modifier, viewModel: LoginViewModel = LoginViewModel()) {

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Email
            TextField(
                modifier = modifier,
                value = email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text("Email") }
            )

            //Password
            TextField(
                modifier = modifier,
                value = password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            //Login Button
            LoginButton(modifier, email, password)


            /* Clear datastore - only use if needed
            Button(
                onClick = {
                    coroutineScope.launch {
                        clearDataStore(context)
                    }

                },

                modifier = modifier,
            ) { Text("Clear DataStore") }
            */

            Text(email)
            Text(password)
        }
    }
}

