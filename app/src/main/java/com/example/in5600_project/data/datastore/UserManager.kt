package com.example.in5600_project.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define the DataStore extension property at the top level
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserManager(private val context: Context) {
    private val USERNAME = stringPreferencesKey("user_name")
    private val PASSWORD = stringPreferencesKey("password")
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    suspend fun saveUserPreferences(username: String, password: String, isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME] = username
            preferences[PASSWORD] = password
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    fun getUserPreferences(): Flow<UserInformation> {
        return context.dataStore.data.map { preferences ->
            UserInformation(
                username = preferences[USERNAME] ?: "",
                password = preferences[PASSWORD] ?: "",
                isLoggedIn = preferences[IS_LOGGED_IN] ?: false
            )
        }
    }
}

data class UserInformation(
    val username: String,
    val password: String,
    val isLoggedIn: Boolean
)
