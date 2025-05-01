package com.example.in5600_project.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavController
import com.example.in5600_project.presentation.ui.components.ChangePasswordButton
import com.example.in5600_project.presentation.ui.components.GoBackButton
import com.example.in5600_project.presentation.viewmodel.ChangePasswordScreenViewModel
import com.example.in5600_project.presentation.viewmodel.MyProfileViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier, viewModel: MyProfileViewModel, navController: NavController
) {

    val changePasswordVM: ChangePasswordScreenViewModel = viewModel()
    val userId by viewModel.currentUserId.collectAsState()
    val newPassword by changePasswordVM.newPassword.collectAsState()

    // Local toggle for showing/hiding the password
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            GoBackButton(navController)
        }, title = {
            Text(
                "Change Password", fontSize = 22.sp, fontWeight = FontWeight.Bold
            )
        })
    }) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Input field for new password
            OutlinedTextField(value = newPassword,
                onValueChange = { changePasswordVM.onNewPasswordChanged(it) },
                label = { Text("New Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password"
                            else "Show password"
                        )
                    }
                },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Button to change password
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                ChangePasswordButton(
                    userId = userId,
                    clearPassword = newPassword,
                )
            }
        }
    }
}
