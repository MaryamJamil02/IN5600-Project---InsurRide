package com.example.in5600_project.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.BasicSecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginTextfield(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    //visualTransformation: PasswordVisualTransformation?
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        placeholder = { Text(placeholder) }
    )


}

