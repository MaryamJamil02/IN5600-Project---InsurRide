package com.example.in5600_project.data.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun methodPostRemoteLogin(
    context: Context,
    email: String,
    hashedPassword: String
): String? = suspendCancellableCoroutine { cont ->

    val queue = Volley.newRequestQueue(context)
    val baseUrl = "http://10.0.2.2:8080/methodPostRemoteLogin"
    val postUrl = "$baseUrl?em=$email&ph=$hashedPassword"

    // Create a StringRequest with a POST method
    val stringRequest = StringRequest(
        Request.Method.POST, postUrl,
        { response ->
            println("Response is: $response")
            cont.resume(response)
        },
        { error ->
            println("Error: ${error.message}")
            cont.resume(null)
        }
    )
    queue.add(stringRequest)
}

