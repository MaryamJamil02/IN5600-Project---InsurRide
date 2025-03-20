package com.example.in5600_project.data.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONArray
import org.json.JSONException
import kotlin.coroutines.resume

suspend fun getMethodMyClaimsDesc(context: Context, id: String): Array<String>? =
    suspendCancellableCoroutine { cont ->
        val queue = Volley.newRequestQueue(context)
        val url = "http://10.0.2.2:8080/getMethodMyClaimsDesc?id=$id"
        val jsonArrayRequest = object : JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener<JSONArray> { response ->
                try {
                    val descArray = Array(response.length()) { i -> response.getString(i) }
                    cont.resume(descArray)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    cont.resume(null)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                cont.resume(null)
            }
        ) {}
        queue.add(jsonArrayRequest)
    }

