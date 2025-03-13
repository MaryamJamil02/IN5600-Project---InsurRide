package com.example.in5600_project.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create Preferences DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserManager(private val context: Context) {

    // A key to store the set of emails
    private val USERS = stringSetPreferencesKey("users")

    // Functions to create keys for each user based on their email
    private fun emailKey(email: String) = stringPreferencesKey("user_${email}_name")
    private fun passwordKey(email: String) = stringPreferencesKey("user_${email}_password")
    private fun isLoggedInKey(email: String) = booleanPreferencesKey("user_${email}_is_logged_in")

    // Save user data in DataStore
    suspend fun saveUserPreferences(email: String, password: String, isLoggedIn: Boolean) {

        context.dataStore.edit { preferences ->

            // Get the current set of users - else initialize an empty set
            val currentUsers = preferences[USERS] ?: emptySet()

            // Add the current email to the set
            val updatedUsers = currentUsers + email

            preferences[USERS] = updatedUsers

            // Save the user data with keys specific to that user
            preferences[emailKey(email)] = email
            preferences[passwordKey(email)] = password
            preferences[isLoggedInKey(email)] = isLoggedIn
        }
    }

    // Return all stored users
    fun getUserPreferences(): Flow<List<UserInformation>> {
        return context.dataStore.data.map { preferences ->
            val users = preferences[USERS] ?: emptySet()
            users.map { user ->
                UserInformation(
                    email = preferences[emailKey(user)] ?: "",
                    password = preferences[passwordKey(user)] ?: "",
                    isLoggedIn = preferences[isLoggedInKey(user)] ?: false
                )
            }
        }
    }

    // Logout a specific user
    suspend fun logoutUser(email: String) {
        context.dataStore.edit { preferences ->
            preferences[isLoggedInKey(email)] = false
        }
    }
}

// Clear current data store
suspend fun clearDataStore(context: Context) {
    context.dataStore.edit { preferences ->
        preferences.clear()
    }
}

// Data class to store user information
data class UserInformation(
    val email: String,
    val password: String,
    val isLoggedIn: Boolean
)
