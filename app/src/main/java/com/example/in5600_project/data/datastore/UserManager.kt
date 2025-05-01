package com.example.in5600_project.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserManager(private val context: Context) {

    // A key to store the set of emails
    private val USERS = stringSetPreferencesKey("users")

    // Functions to create keys for each user based on their id
    private fun userIdKey(id: String) = stringPreferencesKey("user_${id}_id")
    private fun emailKey(id: String) = stringPreferencesKey("user_${id}_mail")
    private fun passwordKey(id: String) = stringPreferencesKey("user_${id}_password")

    // Save user data in DataStore
    suspend fun saveUserPreferences(id: String, email: String, password: String) {

        context.dataStore.edit { preferences ->

            // Get the current set of users, else initialize an empty set
            val currentUsers = preferences[USERS] ?: emptySet()

            // Add the current email to the set
            val updatedUsers = currentUsers + id

            preferences[USERS] = updatedUsers

            // Save the user data with keys specific to that user
            preferences[userIdKey(id)] = id
            preferences[emailKey(id)] = email
            preferences[passwordKey(id)] = password
        }
    }


    suspend fun getUserEmailString(targetUserId: String): String {
        return context.dataStore.data.map { preferences ->
            val users = preferences[USERS] ?: emptySet()
            val matchingUser = users.firstOrNull { user ->
                preferences[userIdKey(user)] == targetUserId
            }
            matchingUser?.let { preferences[emailKey(it)] } ?: ""
        }.first()
    }

    // Change user password
    suspend fun changeUserPassword(id: String, newHashedPassword: String) {
        context.dataStore.edit { preferences ->
            preferences[passwordKey(id)] = newHashedPassword
        }
    }
}

// Clear current data store
suspend fun clearDataStore(context: Context) {
    context.dataStore.edit { preferences ->
        preferences.clear()
    }
}
