package com.example.in5600_project.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyProfileViewModel : ViewModel() {

    private val _currentUserId = MutableStateFlow("")
    val currentUserId = _currentUserId.asStateFlow()

    fun onUserIdChanged(newUserId: String) {
        _currentUserId.value = newUserId
    }

}