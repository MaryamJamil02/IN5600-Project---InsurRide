package com.example.in5600_project.data.network

import User
import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import isUserStored
import kotlinx.coroutines.runBlocking
import saveUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun methodPostRemoteLogin(context: Context, email: String, hashedPassword: String): Boolean {
    // Check DataStore first
    if (isUserStored(context, email, hashedPassword)) {
        return true // User found in DataStore → Login Successful
    }

    // Otherwise, check with server
    return withContext(Dispatchers.IO) {
        val queue = Volley.newRequestQueue(context)

        val baseUrl = "http://10.0.2.2:8080/methodPostRemoteLogin"
        val postUrl = "$baseUrl?em=$email&ph=$hashedPassword"
        var loginSuccess = false

        val request = StringRequest(Request.Method.GET, postUrl, { response ->
            if (response == "OK") {

                    saveUser(context, User(email, hashedPassword)) // Save user if login is valid

                loginSuccess = true
            }
        }, { error ->
            loginSuccess = false
        })

        queue.add(request)
        loginSuccess
    }
}


