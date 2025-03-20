import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONException
import kotlin.coroutines.resume

// Data class matching the server's Person class
data class Person(
    val id: String,
    val firstName: String,
    val lastName: String,
    val passClear: String,
    val passHash: String,
    val email: String
)

suspend fun methodPostRemoteLogin(
    context: Context,
    email: String,
    hashedPassword: String
): Person? = suspendCancellableCoroutine { cont ->

    val queue = Volley.newRequestQueue(context)
    val baseUrl = "http://10.0.2.2:8080/methodPostRemoteLogin"
    val postUrl = "$baseUrl?em=$email&ph=$hashedPassword"

    val jsonObjectRequest = object : JsonObjectRequest(
        Request.Method.POST,
        postUrl,
        null,
        Response.Listener { response ->
            try {
                // Parse the JSON response into a Person object
                val person = Person(
                    id = response.getString("id"),
                    firstName = response.getString("firstName"),
                    lastName = response.getString("lastName"),
                    passClear = response.getString("passClear"),
                    passHash = response.getString("passHash"),
                    email = response.getString("email")
                )
                cont.resume(person)
            } catch (e: JSONException) {
                e.printStackTrace()
                cont.resume(null)
            }
        },
        Response.ErrorListener { error ->
            error.printStackTrace()
            cont.resume(null)
        }
    ) {
    }

    queue.add(jsonObjectRequest)
}
