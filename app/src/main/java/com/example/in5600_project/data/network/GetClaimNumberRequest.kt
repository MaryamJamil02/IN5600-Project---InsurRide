package com.example.in5600_project.data.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


suspend fun getMyClaimsNumber(context: Context, id: String): String? =
    suspendCancellableCoroutine { cont ->
        val queue = Volley.newRequestQueue(context)
        val url = "http://10.0.2.2:8080/getMethodMyClaimsNumber?id=$id"
        val stringRequest = object : StringRequest(
            Request.Method.GET,
            url,
            Response.Listener { response ->
                println("Response is: $response")
                cont.resume(response)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                cont.resume(null)
            }
        ) {}
        queue.add(stringRequest)
    }

