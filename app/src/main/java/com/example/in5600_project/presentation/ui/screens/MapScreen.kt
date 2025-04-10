package com.example.in5600_project.presentation.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.in5600_project.data.network.getMethodDownloadPhoto
import androidx.compose.foundation.Image as Image

@Composable
fun MapScreen(modifier: Modifier) {
    val hardcodedFilePath = "/storage/emulated/0/Pictures/Screenshots/1000000025.png"
    val imageUri = Uri.parse(hardcodedFilePath)
    val context = LocalContext.current


    Box(modifier = modifier.background(Color.LightGray).fillMaxSize()) {
        Text("Welcome to Maps")

        Spacer(modifier = modifier.height(4.dp))

        DisplayClaimImage("1000000025", context)
    }
}

