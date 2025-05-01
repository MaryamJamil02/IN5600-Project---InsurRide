package com.example.in5600_project.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.in5600_project.R
import com.example.in5600_project.data.datastore.clearDataStore
import com.example.in5600_project.navigation.AppBottomBar
import com.example.in5600_project.presentation.ui.theme.SecondaryDark
import com.example.in5600_project.presentation.ui.theme.PrimaryDark
import com.example.in5600_project.presentation.viewmodel.LoginViewModel
import com.example.in5600_project.presentation.viewmodel.MyProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    navController: NavController, viewModel: MyProfileViewModel, loginViewModel: LoginViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userId by viewModel.currentUserId.collectAsState()
    val email by loginViewModel.email.collectAsState()

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "My Profile", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = PrimaryDark, titleContentColor = Color.White
            )
        )
    }, bottomBar = { AppBottomBar(navController) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Profile picture
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(PrimaryDark),
                contentAlignment = Alignment.BottomEnd
            ) {
                ProfilePicture()
            }

            Spacer(modifier = Modifier.height(24.dp))

            // User Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "User ID:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(text = userId.takeIf { it.isNotBlank() } ?: "N/A",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Email:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(text = email.takeIf { it.isNotBlank() } ?: "N/A",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Logout and Change Password Buttons
            Column(modifier = Modifier.fillMaxWidth()) {

                OutlinedButton(
                    onClick = { navController.navigate("changePasswordScreen") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Change Password",
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            clearDataStore(context)
                            loginViewModel.clearFields()
                            withContext(Dispatchers.Main) {
                                navController.navigate("loginScreen") {
                                    popUpTo("myProfileScreen") { inclusive = true }
                                }
                            }
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// Profile picture
@Composable
fun ProfilePicture() {
    Image(
        painter = painterResource(id = R.drawable.person),
        contentDescription = "Profile Image",
        modifier = Modifier.size(110.dp)
    )
}
