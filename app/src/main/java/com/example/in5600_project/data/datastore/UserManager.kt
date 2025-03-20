package com.example.in5600_project.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create Preferences DataStore
//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserManager(private val context: Context) {

    // A key to store the set of emails
    private val USERS = stringSetPreferencesKey("users")

    // Functions to create keys for each user based on their id
    private fun userIdKey(id: String) = stringPreferencesKey("user_${id}_id")
    private fun emailKey(id: String) = stringPreferencesKey("user_${id}_mail")
    private fun passwordKey(id: String) = stringPreferencesKey("user_${id}_password")
    private fun isLoggedInKey(id: String) = booleanPreferencesKey("user_${id}_is_logged_in")

    // Save user data in DataStore
    suspend fun saveUserPreferences(id: String, email: String, password: String, isLoggedIn: Boolean) {

        context.dataStore.edit { preferences ->

            // Get the current set of users - else initialize an empty set
            val currentUsers = preferences[USERS] ?: emptySet()

            // Add the current email to the set
            val updatedUsers = currentUsers + id

            preferences[USERS] = updatedUsers

            // Save the user data with keys specific to that user
            preferences[userIdKey(id)] = id
            preferences[emailKey(id)] = email
            preferences[passwordKey(id)] = password
            preferences[isLoggedInKey(id)] = isLoggedIn
        }
    }

    // Return all stored users
    fun getUserPreferences(): Flow<List<UserInformation>> {
        return context.dataStore.data.map { preferences ->
            val users = preferences[USERS] ?: emptySet()
            users.map { user ->
                UserInformation(
                    id = preferences[userIdKey(user)] ?: "",
                    email = preferences[emailKey(user)] ?: "",
                    password = preferences[passwordKey(user)] ?: "",
                    isLoggedIn = preferences[isLoggedInKey(user)] ?: false
                )
            }
        }
    }

    // Logout a specific user
    suspend fun logoutUser( id: String) {
        context.dataStore.edit { preferences ->
            preferences[isLoggedInKey(id)] = false
        }
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

// Data class to store user information
data class UserInformation(
    val id: String,
    val email: String,
    val password: String,
    val isLoggedIn: Boolean
)
