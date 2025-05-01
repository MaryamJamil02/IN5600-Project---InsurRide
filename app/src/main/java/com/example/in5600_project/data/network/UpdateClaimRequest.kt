package com.example.in5600_project.data.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun postUpdateClaim(
    context: Context,
    userId: String,
    indexUpdateClaim: String,
    updateClaimDescription: String,
    updateClaimPhoto: String,
    updateClaimLocation: String,
    updateClaimStatus: String
): String? = suspendCancellableCoroutine { cont ->

    val queue = Volley.newRequestQueue(context)
    val baseUrl = "http://10.0.2.2:8080/postUpdateClaim"
    val postUrl =
        "$baseUrl?userId=$userId&indexUpdateClaim=$indexUpdateClaim&updateClaimDes=$updateClaimDescription&updateClaimPho=$updateClaimPhoto&updateClaimLoc=$updateClaimLocation&updateClaimSta=$updateClaimStatus"

    // Create a StringRequest with a POST method
    val stringRequest =
        object : StringRequest(Request.Method.POST, postUrl, Response.Listener { response ->
            cont.resume(response)
        }, Response.ErrorListener { error ->
            error.printStackTrace()
            cont.resume(null)
        }) {}

    queue.add(stringRequest)
}
