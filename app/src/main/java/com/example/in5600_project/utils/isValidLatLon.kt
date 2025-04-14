package com.example.in5600_project.utils

fun isValidLatLon(coordinate: String?): Boolean {
    if (coordinate.isNullOrEmpty()) {
        return false
    }

    // Attempt to split by comma
    val parts = coordinate.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    if (parts.size != 2) {
        return false
    }

    try {
        val lat = parts[0].trim { it <= ' ' }.toDouble()
        val lon = parts[1].trim { it <= ' ' }.toDouble()

        // Check latitude range
        if (lat < -90.0 || lat > 90.0) {
            return false
        }
        // Check longitude range
        if (lon < -180.0 || lon > 180.0) {
            return false
        }

        // If we get here, both values are valid
        return true
    } catch (e: NumberFormatException) {
        // Parsing failed
        return false
    }
}