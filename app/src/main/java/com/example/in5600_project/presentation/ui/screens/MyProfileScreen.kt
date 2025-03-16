import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.in5600_project.data.datastore.UserManager
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.example.in5600_project.presentation.ui.components.LogoutButton
import kotlinx.coroutines.runBlocking

@Composable
fun MyProfileScreen(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    val userManager = UserManager(context)
    val coroutineScope = rememberCoroutineScope()

    var currentUserEmail: String? = null

    // Retrieve the current logged-in user's email
    runBlocking {
        currentUserEmail = userManager.getLoggedInUserEmail()
    }

    // Make sure we have a valid email
    if (currentUserEmail != null) {

        Box(modifier = modifier.padding(16.dp)) {
            Text("Welcome to My Profile")
            Text("Logged in as: $currentUserEmail")
            LogoutButton(
                modifier = Modifier.padding(top = 16.dp),
                email = currentUserEmail!!,
                navController = navController,
                coroutineScope = coroutineScope
            )
        }
    } else {
        // Handle case when no user is logged in (maybe redirect to login screen)
        Text("No user is logged in.")
    }
}