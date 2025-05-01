package com.example.in5600_project.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Create a singleton DataStore instance
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
