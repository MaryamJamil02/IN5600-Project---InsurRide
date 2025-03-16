package com.example.in5600_project.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class ChangePasswordScreenViewModel : ViewModel() {

    // New password state for input field
    private val _newPassword = MutableStateFlow("")
    val newPassword = _newPassword.asStateFlow()

    fun onNewPasswordChanged(newPassword: String) {
        _newPassword.value = newPassword
    }
}
