package com.example.in5600_project.data.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun postInsertNewClaim(
    context: Context,
    userId: String,
    indexUpdateClaim: String,
    newClaimDes: String,
    newClaimPhoto: String,
    newClaimLocation: String,
    newClaimStatus: String
): String? = suspendCancellableCoroutine { cont ->

    val queue = Volley.newRequestQueue(context)
    val baseUrl = "http://10.0.2.2:8080/postInsertNewClaim"
    val postUrl = "$baseUrl?userId=$userId&indexUpdateClaim=$indexUpdateClaim&newClaimDes=$newClaimDes&newClaimPho=$newClaimPhoto&newClaimLoc=$newClaimLocation&newClaimSta=$newClaimStatus"

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
