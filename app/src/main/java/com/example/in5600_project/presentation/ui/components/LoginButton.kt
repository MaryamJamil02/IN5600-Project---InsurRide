// LoginButton.kt
package com.example.in5600_project.presentation.ui.components

import android.util.Base64
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.in5600_project.data.datastore.UserManager
import com.example.in5600_project.data.datastore.ClaimsManager
import com.example.in5600_project.data.network.getMethodMyClaimsDesc
import com.example.in5600_project.data.network.getMethodMyClaimsIds
import com.example.in5600_project.data.network.getMethodMyClaimsLocation
import com.example.in5600_project.data.network.getMethodMyClaimsNumber
import com.example.in5600_project.data.network.getMethodMyClaimsPhoto
import com.example.in5600_project.data.network.getMethodMyClaimsStatus
import com.example.in5600_project.data.network.getMethodDownloadPhoto
import com.example.in5600_project.utils.hashPassword
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.in5600_project.presentation.viewmodel.MyProfileViewModel
import kotlinx.coroutines.flow.first
import methodPostRemoteLogin
import java.io.File
import java.io.FileOutputStream

@Composable
fun LoginButton(
    modifier: Modifier,
    email: String,
    password: String,
    myProfileViewModel: MyProfileViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val userManager = UserManager(context)
    val claimsManager = ClaimsManager(context)
    val coroutineScope = rememberCoroutineScope()
    var successfullyLoggedIn = false
    var currentUserId = ""

    Button(modifier = modifier, onClick = {

        // Hash the password before sending it to the server
        val hashedPassword: String = hashPassword(password)

        coroutineScope.launch {

            // Get the current list of the stored users
            val users = userManager.getUserPreferences().first()

            // Check if a user with the provided email is already logged in
            if (users.any { it.email == email && it.password == hashedPassword}) {
                successfullyLoggedIn = true
                currentUserId = users.first { it.email == email }.id
                Toast.makeText(context, "Local login successful", Toast.LENGTH_SHORT).show()
            }

            // Remote login
            else{

                // Attempt remote login
                val responseLogin = methodPostRemoteLogin(context, email, hashedPassword)

                if (responseLogin != null) {
                    userManager.saveUserPreferences(
                        responseLogin.id, responseLogin.email, hashedPassword
                    )
                    currentUserId = responseLogin.id
                    successfullyLoggedIn = true
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            // If login was successful, fetch claims data
            if (successfullyLoggedIn) {

                // Fetch claims metadata
                val claimsNumber = getMethodMyClaimsNumber(context, currentUserId)
                val claimsIds = getMethodMyClaimsIds(context, currentUserId)
                val claimsList = getMethodMyClaimsDesc(context, currentUserId)
                val claimsPhoto = getMethodMyClaimsPhoto(context, currentUserId)
                val claimsLocation = getMethodMyClaimsLocation(context, currentUserId)
                val claimsStatus = getMethodMyClaimsStatus(context, currentUserId)

                if (claimsNumber != null && claimsIds != null && claimsList != null && claimsPhoto != null && claimsLocation != null && claimsStatus != null) {
                    // Download and cache images
                    claimsPhoto.forEach { fileName ->
                        val base64String = getMethodDownloadPhoto(context, fileName)
                        if (base64String != null) {
                            val imageBytes = Base64.decode(
                                base64String, Base64.NO_WRAP or Base64.URL_SAFE
                            )
                            // Save the image to the cache
                            val cacheFile = File(context.filesDir, fileName)
                            FileOutputStream(cacheFile).use { it.write(imageBytes) }
                        }
                    }

                    // Save claims (with file names) in DataStore
                    claimsManager.saveUserClaims(
                        currentUserId,
                        claimsNumber,
                        claimsIds,
                        claimsList,
                        claimsPhoto,
                        claimsLocation,
                        claimsStatus
                    )
                }

                // Set the current user's email for logout
                myProfileViewModel.onUserIdChanged(currentUserId)
                navController.navigate("claimsHomeScreen")
            }
        }
    }) {
        Text("Login")
    }
}
