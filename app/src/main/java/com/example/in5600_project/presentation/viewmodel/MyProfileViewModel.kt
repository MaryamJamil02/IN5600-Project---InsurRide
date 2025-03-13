package com.example.in5600_project.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyProfileViewModel : ViewModel() {

    private val _currentemail = MutableStateFlow("")
    val currentemail = _currentemail.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _currentemail.value = newEmail
    }

}