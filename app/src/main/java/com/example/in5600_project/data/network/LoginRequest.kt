package com.example.in5600_project.data.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


fun makeGetRequest(context: Context) {
    val queue = Volley.newRequestQueue(context)
    val url = "http://10.0.2.2:8080/getMethodTesting"
    val stringRequest = StringRequest(
        Request.Method.GET, url,
        { response ->
            println("Response is: $response")
        },
        { error ->
            println("Error: ${error.message}")
        }
    )
    queue.add(stringRequest)
}


fun methodPostRemoteLogin(context: Context, email: String, hashedPassword: String) {
    val queue: RequestQueue = Volley.newRequestQueue(context)
    val baseurl = "http://10.0.2.2:8080/methodPostRemoteLogin"

    // Add parameters to th base URL
    val postUrl = "$baseurl?em=$email&ph=$hashedPassword"

    val stringRequest = StringRequest(
        Request.Method.POST, postUrl,
        { response ->
            println("Response is: $response")
        },
        { error ->
            println("Error: ${error.message}")
        }
    )

    queue.add(stringRequest)
}