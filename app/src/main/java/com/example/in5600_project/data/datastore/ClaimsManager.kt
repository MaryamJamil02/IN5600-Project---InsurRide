package com.example.in5600_project.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Data class to represent a claim.
data class ClaimInformation(
    val claimId: String,
    val claimDes: String,
    val claimPhoto: String,
    val claimLocation: String,
    val claimStatus: String
)

// Define a key to store the claims for a specific user (using the user's id).
private fun numberOfClaimsKey(id: String) = stringPreferencesKey("user_${id}_nr_of_claims")
private fun claimsKey(id: String) = stringPreferencesKey("user_${id}_claims")

class ClaimsManager(private val context: Context) {
    private val gson = Gson()

    // Save claims for a user by serializing the list into a JSON string.
    suspend fun saveUserClaims(id: String, numberOfClaims: String, claims: Array<String>?) {
        val claimsJson = gson.toJson(claims)
        context.dataStore.edit { preferences ->
            preferences[numberOfClaimsKey(id)] = numberOfClaims
            preferences[claimsKey(id)] = claimsJson
        }
    }

    // Retrieve the claims for a user by deserializing the stored JSON string.
    fun getUserClaims(id: String): Flow<List<String>?> {
        return context.dataStore.data.map { preferences ->
            val claimsJson = preferences[claimsKey(id)] ?: "[]"
            gson.fromJson(claimsJson, Array<String>::class.java).toList()
        }
    }
}
