package com.example.in5600_project.utils

import android.content.Context
import android.net.Uri
import android.util.Base64
import java.io.File

fun decodeBase64ToUri(context: Context, base64Str: String, fileName: String): Uri? {
    return try {
        // Decode the Base64 string to bytes
        val bytes = Base64.decode(base64Str, Base64.NO_WRAP or Base64.URL_SAFE)

        // Write bytes to a file in the cache directory
        val file = File(context.cacheDir, fileName)
        file.writeBytes(bytes)

        // Return the URI for the file
        Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

