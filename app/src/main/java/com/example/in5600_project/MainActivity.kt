package com.example.in5600_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.in5600_project.navigation.*
import com.example.in5600_project.presentation.ui.theme.IN5600ProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IN5600ProjectTheme {
                Surface {
                    MultipleScreenNavigator(modifier = Modifier)
                }
            }
        }
    }
}

