package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.in5600_project.presentation.ui.components.ChangePasswordButton
import com.example.in5600_project.presentation.viewmodel.ChangePasswordScreenViewModel
import com.example.in5600_project.presentation.viewmodel.MyProfileViewModel
import androidx.lifecycle.viewmodel.compose.*
import com.example.in5600_project.presentation.ui.components.GoBackButton


@Composable
fun ChangePasswordScreen(
    modifier: Modifier,
    viewModel: MyProfileViewModel,
    navController: NavController
) {

    val changePasswordScreenViewModel: ChangePasswordScreenViewModel = viewModel()
    val userId by viewModel.currentUserId.collectAsState()
    val newPassword by changePasswordScreenViewModel.newPassword.collectAsState()

    Column(
        modifier = modifier
            .background(Color.LightGray)
            .fillMaxSize()
            .padding(top = 30.dp)
    )
    {

        GoBackButton(navController, isPopBackStack = true)

        Text("Change Password")

        // Create new password
        OutlinedTextField(
            value = newPassword,
            onValueChange = { changePasswordScreenViewModel.onNewPasswordChanged(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Text(newPassword)

        ChangePasswordButton(userId, newPassword)
    }


}
