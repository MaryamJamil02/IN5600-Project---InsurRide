package com.example.in5600_project.utils

import java.security.MessageDigest

// Hash password from clear text to SHA-512 hash
@OptIn(ExperimentalStdlibApi::class)
fun hashPassword(password: String): String {
    val md = MessageDigest.getInstance("SHA-512")
    val digest = md.digest(password.toByteArray())
    return digest.toHexString()
}