import android.content.Context
import kotlinx.serialization.*
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

// Define DataStore
val Context.dataStore by preferencesDataStore(name = "users_prefs")

// Keys for storing users
val USERS_KEY = stringPreferencesKey("users") // Stores a list of users as JSON


@Serializable
data class User(val username: String, val password: String)

suspend fun saveUser(context: Context, newUser: User) {
    context.dataStore.edit { preferences ->
        val usersJson = preferences[USERS_KEY] ?: "[]"
        val usersList = Json.decodeFromString<List<User>>(usersJson).toMutableList()

        // Add user if they don't exist
        if (usersList.none { it.username == newUser.username }) {
            usersList.add(newUser)
            preferences[USERS_KEY] = Json.encodeToString(usersList)
        }
    }
}

suspend fun isUserStored(context: Context, username: String, password: String): Boolean {
    return context.dataStore.data.first()[USERS_KEY]?.let { usersJson ->
        val usersList = Json.decodeFromString<List<User>>(usersJson)
        usersList.any { it.username == username && it.password == password }
    } ?: false
}

