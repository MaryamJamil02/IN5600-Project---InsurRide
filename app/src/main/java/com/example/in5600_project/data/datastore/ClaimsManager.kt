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
private fun claimsDescKey(id: String) = stringPreferencesKey("user_${id}_claims_desc")
private fun claimsPhotoKey(id: String) = stringPreferencesKey("user_${id}_claims_photo")
private fun claimsLocationKey(id: String) = stringPreferencesKey("user_${id}_claims_location")
private fun claimsStatusKey(id: String) = stringPreferencesKey("user_${id}_claims_status")

class ClaimsManager(private val context: Context) {
    private val gson = Gson()

    // Save claims for a user by serializing the list into a JSON string.
    suspend fun saveUserClaims(id: String, numberOfClaims: String, claimsDesc: Array<String>?, claimsPhoto: Array<String>?, claimsLocation: Array<String>?, claimsStatus: Array<String>) {
        val claimsDescJson = gson.toJson(claimsDesc)
        val claimsPhotoJson = gson.toJson(claimsPhoto)
        val claimsLocationJson = gson.toJson(claimsLocation)
        val claimsStatusJson = gson.toJson(claimsStatus)

        context.dataStore.edit { preferences ->
            preferences[numberOfClaimsKey(id)] = numberOfClaims
            preferences[claimsDescKey(id)] = claimsDescJson
            preferences[claimsPhotoKey(id)] = claimsPhotoJson
            preferences[claimsLocationKey(id)] = claimsLocationJson
            preferences[claimsStatusKey(id)] = claimsStatusJson
        }
    }

    fun getUserClaims(id: String): Flow<List<ClaimInformation>> {
        return context.dataStore.data.map { preferences ->
            // Get the number of claims from the stored string, defaulting to 0.
            val numberOfClaimsStr = preferences[numberOfClaimsKey(id)] ?: "0"
            val numberOfClaims = numberOfClaimsStr.toIntOrNull() ?: 0

            // Retrieve each field's JSON array from the preferences.
            val claimsDescJson = preferences[claimsDescKey(id)] ?: "[]"
            val claimsPhotoJson = preferences[claimsPhotoKey(id)] ?: "[]"
            val claimsLocationJson = preferences[claimsLocationKey(id)] ?: "[]"
            val claimsStatusJson = preferences[claimsStatusKey(id)] ?: "[]"

            // Deserialize the JSON arrays into Kotlin arrays.
            val claimsDescArray = gson.fromJson(claimsDescJson, Array<String>::class.java)
            val claimsPhotoArray = gson.fromJson(claimsPhotoJson, Array<String>::class.java)
            val claimsLocationArray = gson.fromJson(claimsLocationJson, Array<String>::class.java)
            val claimsStatusArray = gson.fromJson(claimsStatusJson, Array<String>::class.java)

            // Combine the arrays into a list of ClaimInformation objects.
            // The claimId is generated as the index string.
            (0 until numberOfClaims).map { index ->
                ClaimInformation(
                    claimId = index.toString(),
                    claimDes = claimsDescArray.getOrElse(index) { "" },
                    claimPhoto = claimsPhotoArray.getOrElse(index) { "" },
                    claimLocation = claimsLocationArray.getOrElse(index) { "" },
                    claimStatus = claimsStatusArray.getOrElse(index) { "" }
                )
            }
        }
    }

}
