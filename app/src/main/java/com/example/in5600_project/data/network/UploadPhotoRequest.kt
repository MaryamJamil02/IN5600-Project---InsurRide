package com.example.in5600_project.data.network

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.InputStream
import kotlin.coroutines.resume

suspend fun postMethodUploadPhoto(
    context: Context,
    userId: String,
    claimId: String,
    fileName: String,
    imageUri: Uri
): String? = suspendCancellableCoroutine { cont ->

    val imageStringBase64 = convertImageUriToBase64(context, imageUri)
    if (imageStringBase64 == null) {
        cont.resume(null)
        return@suspendCancellableCoroutine
    }

    val queue = Volley.newRequestQueue(context)
    val baseUrl = "http://10.0.2.2:8080/postMethodUploadPhoto"
    val postUrl = "$baseUrl?userId=$userId&claimId=$claimId&fileName=$fileName&imageStringBase64=$imageStringBase64"

    val stringRequest = object : StringRequest(
        Request.Method.POST,
        postUrl,
        Response.Listener { response ->
            cont.resume(response)
        },
        Response.ErrorListener { error ->
            error.printStackTrace()
            cont.resume(null)
        }
    ) {}

    queue.add(stringRequest)
}

fun convertImageUriToBase64(context: Context, imageUri: Uri): String? {
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return if (bytes != null) {
            Base64.encodeToString(bytes, Base64.NO_WRAP or Base64.URL_SAFE)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}