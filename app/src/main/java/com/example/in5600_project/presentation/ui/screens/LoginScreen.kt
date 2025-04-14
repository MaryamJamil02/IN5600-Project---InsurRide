package com.example.in5600_project.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.clearDataStore
import com.example.in5600_project.presentation.ui.components.LoginButton
import com.example.in5600_project.presentation.viewmodel.LoginViewModel
import com.example.in5600_project.presentation.viewmodel.MyProfileViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(modifier: Modifier, viewModel: LoginViewModel = LoginViewModel(), myProfileViewModel: MyProfileViewModel, navController: NavController) {

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

            Text("Login")

            //Email
            OutlinedTextField(
                modifier = modifier,
                value = email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text("Email") }
            )

            //Password
            OutlinedTextField(
                modifier = modifier,
                value = password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            //Login Button
            LoginButton(modifier, email, password, myProfileViewModel, navController)


            Text(email)
            Text(password)
        }
    }
}

