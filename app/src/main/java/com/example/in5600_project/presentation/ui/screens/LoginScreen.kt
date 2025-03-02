package com.example.in5600_project.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(modifier: Modifier){

    Scaffold(
        modifier = modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TextField(
                value = "",
                onValueChange = {},
                label = { Text("E-mail") },
                modifier = Modifier.padding(16.dp),
                placeholder = { Text("user@mail.com") }
            )

            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Password") },
                modifier = Modifier.padding(16.dp),
                placeholder = { Text("****") }
            )
        }
    }
}