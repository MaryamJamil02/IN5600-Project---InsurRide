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
private fun claimsIdKey(id: String) = stringPreferencesKey("user_${id}_claims_id")
private fun claimsDescKey(id: String) = stringPreferencesKey("user_${id}_claims_desc")
private fun claimsPhotoKey(id: String) = stringPreferencesKey("user_${id}_claims_photo")
private fun claimsLocationKey(id: String) = stringPreferencesKey("user_${id}_claims_location")
private fun claimsStatusKey(id: String) = stringPreferencesKey("user_${id}_claims_status")

class ClaimsManager(private val context: Context) {
    private val gson = Gson()

    // Maximum number of claims allowed per user
    private val maxclaims = 5

    // Save claims for a user by serializing the list into a JSON string.
    suspend fun saveUserClaims(
        id: String,
        numberOfClaims: String,
        claimsIds: Array<String>?,
        claimsDesc: Array<String>?,
        claimsPhoto: Array<String>?,
        claimsLocation: Array<String>?,
        claimsStatus: Array<String>
    ) {

        val claimsIdsJson = gson.toJson(claimsIds)
        val claimsDescJson = gson.toJson(claimsDesc)
        val claimsPhotoJson = gson.toJson(claimsPhoto)
        val claimsLocationJson = gson.toJson(claimsLocation)
        val claimsStatusJson = gson.toJson(claimsStatus)

        context.dataStore.edit { preferences ->
            preferences[numberOfClaimsKey(id)] = numberOfClaims
            preferences[claimsIdKey(id)] = claimsIdsJson
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
            val claimsIdJson = preferences[claimsIdKey(id)] ?: "[]"
            val claimsDescJson = preferences[claimsDescKey(id)] ?: "[]"
            val claimsPhotoJson = preferences[claimsPhotoKey(id)] ?: "[]"
            val claimsLocationJson = preferences[claimsLocationKey(id)] ?: "[]"
            val claimsStatusJson = preferences[claimsStatusKey(id)] ?: "[]"

            // Deserialize the JSON arrays into Kotlin arrays.
            val claimsIdsArray = gson.fromJson(claimsIdJson, Array<String>::class.java)
            val claimsDescArray = gson.fromJson(claimsDescJson, Array<String>::class.java)
            val claimsPhotoArray = gson.fromJson(claimsPhotoJson, Array<String>::class.java)
            val claimsLocationArray = gson.fromJson(claimsLocationJson, Array<String>::class.java)
            val claimsStatusArray = gson.fromJson(claimsStatusJson, Array<String>::class.java)

            // Combine the arrays into a list of ClaimInformation objects
            (0 until numberOfClaims).map { index ->
                ClaimInformation(
                    claimId = claimsIdsArray.getOrElse(index) { "" },
                    claimDes = claimsDescArray.getOrElse(index) { "" },
                    claimPhoto = claimsPhotoArray.getOrElse(index) { "" },
                    claimLocation = claimsLocationArray.getOrElse(index) { "" },
                    claimStatus = claimsStatusArray.getOrElse(index) { "" }
                )
            }
        }
    }

    fun getNumberOfClaims(id: String): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            (preferences[numberOfClaimsKey(id)] ?: "0").toIntOrNull() ?: 0
        }
    }


    suspend fun updateOrInsertClaimAtIndex(
        userId: String,
        index: Int,
        newClaim: ClaimInformation,
        isInsert: Boolean
    ) {
        context.dataStore.edit { preferences ->
            // Get the current number of valid claims.
            val numberOfClaimsStr = preferences[numberOfClaimsKey(userId)] ?: "0"
            var numberOfClaims = numberOfClaimsStr.toIntOrNull() ?: 0

            // Get the stored JSON arrays and convert them into mutable lists.
            val claimsIdsList =
                gson.fromJson(preferences[claimsIdKey(userId)] ?: "[]", Array<String>::class.java)
                    .toMutableList()
            val claimsDescList =
                gson.fromJson(preferences[claimsDescKey(userId)] ?: "[]", Array<String>::class.java)
                    .toMutableList()
            val claimsPhotoList = gson.fromJson(
                preferences[claimsPhotoKey(userId)] ?: "[]",
                Array<String>::class.java
            ).toMutableList()
            val claimsLocationList = gson.fromJson(
                preferences[claimsLocationKey(userId)] ?: "[]",
                Array<String>::class.java
            ).toMutableList()
            val claimsStatusList = gson.fromJson(
                preferences[claimsStatusKey(userId)] ?: "[]",
                Array<String>::class.java
            ).toMutableList()


            //println("inserttt $isInsert")


            if (isInsert) {
                // Find the first vacant spot (where claimId is blank or "na").
                var insertIndex: Int? = null
                for (i in 0 until maxclaims) {
                    println("Inserttttt for")
                    println("claimIdd: " + claimsIdsList[i])
                    println("claimId isempty: " + claimsIdsList[i].isEmpty())
                    println("claimId isblank: " + claimsIdsList[i].isBlank())

                    //println("claimId isnull: " + (claimsIdsList[i] == null))
                    println("claimId isnullString: " + (claimsIdsList[i] == "null"))
                    println("claimId isNaStreng: " + (claimsIdsList[i] == "na"))




                    if (claimsIdsList[i] == "null" || claimsIdsList[i] == "na") {
                        insertIndex = i
                        println("Inserttttt if $insertIndex")
                        break
                    }
                }
                if (insertIndex != null) {
                    // Insert the new claim at the vacant spot.
                    claimsIdsList[insertIndex] = newClaim.claimId
                    claimsDescList[insertIndex] = newClaim.claimDes
                    claimsPhotoList[insertIndex] = newClaim.claimPhoto
                    claimsLocationList[insertIndex] = newClaim.claimLocation
                    claimsStatusList[insertIndex] = newClaim.claimStatus

                    // Update the number of claims
                    numberOfClaims++
                    preferences[numberOfClaimsKey(userId)] = numberOfClaims.toString()

                }
            } else {
                // Update the claim at the given index.
                println("Updateeee index $index")
                println("Updateeee maxclaims $maxclaims")
                if (index < maxclaims) {
                    println("Updateeee if")
                    claimsIdsList[index] = newClaim.claimId
                    claimsDescList[index] = newClaim.claimDes
                    claimsPhotoList[index] = newClaim.claimPhoto
                    claimsLocationList[index] = newClaim.claimLocation
                    claimsStatusList[index] = newClaim.claimStatus
                }
            }

            // Save the updated arrays back to DataStore.
            preferences[claimsIdKey(userId)] = gson.toJson(claimsIdsList)
            preferences[claimsDescKey(userId)] = gson.toJson(claimsDescList)
            preferences[claimsPhotoKey(userId)] = gson.toJson(claimsPhotoList)
            preferences[claimsLocationKey(userId)] = gson.toJson(claimsLocationList)
            preferences[claimsStatusKey(userId)] = gson.toJson(claimsStatusList)
        }
    }


}
