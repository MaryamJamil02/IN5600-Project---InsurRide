package com.example.in5600_project.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.in5600_project.presentation.ui.components.LoginButton
import com.example.in5600_project.presentation.viewmodel.LoginViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(modifier: Modifier, viewModel: LoginViewModel = LoginViewModel()){

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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


            LoginButton(modifier, email, password)

            Text(email)
            Text(password)
        }
    }
}

