package com.elfennani.readit.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.elfennani.readit.data.local.User
import com.elfennani.readit.data.local.UserDao
import com.elfennani.readit.utilities.AUTH_REDIRECT
import com.elfennani.readit.utilities.AUTH_SCOPES
import com.elfennani.readit.utilities.AUTH_STATE
import com.elfennani.readit.utilities.AUTH_TOKEN_URL
import com.elfennani.readit.utilities.AUTH_URL
import com.elfennani.readit.utilities.BASE_URL
import com.elfennani.readit.utilities.CLIENT_ID
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.jsoup.Jsoup
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Base64
import javax.inject.Inject

//https://www.reddit.com/api/v1/authorize?client_id=jZxbCi8nroDGjr_X7ss5AQ&response_type=code&state=READIT1&redirect_uri=readit%3A%2F%2Fredirect&duration=permanent&scope=identity%2Cedit%2Cflair%2Chistory%2Cmodconfig%2Cmodflair%2Cmodlog%2Cmodposts%2Cmodwiki%2Cmysubreddits%2Cprivatemessages%2Cread%2Creport%2Csave%2Csubmit%2Csubscribe%2Cvote%2Cwikiedit%2Cwikiread

data class TokenRetrievalResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Long,
    @SerializedName("refresh_token") val refreshToken: String,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    val application: Application,
    private val userDao: UserDao
) : ViewModel() {
    private val client = OkHttpClient();
    fun initiateLogin(context: Context) {
        val uri = buildUrl(
            AUTH_URL, mapOf<String, String>(
                "client_id" to CLIENT_ID,
                "response_type" to "code",
                "state" to AUTH_STATE,
                "redirect_uri" to AUTH_REDIRECT,
                "duration" to "permanent",
                "scope" to AUTH_SCOPES.joinToString(separator = ",")
            )
        )

        val browserIntent = Intent(Intent.ACTION_VIEW, uri)
        (context as Activity).startActivity(browserIntent)
    }

    private fun buildUrl(url: String, params: Map<String, String>): Uri {
        val uri = Uri.parse(url).buildUpon()
        params.forEach {
            uri.appendQueryParameter(it.key, it.value)
        }

        return uri.build()
    }

    private fun encode(value: String): String {
        return java.net.URLEncoder.encode(value, "UTF-8")
    }

    private suspend fun saveUser(tokenRetrievalResponse: TokenRetrievalResponse) {
        val url = "$BASE_URL/api/v1/me";
        val request = Request.Builder().url(url)
            .addHeader("Authorization", "Bearer ${tokenRetrievalResponse.accessToken}")
            .get()
            .build()

        val res = client.newCall(request).execute()
        val body = res.body?.string() ?: throw Exception("Body Empty")

        val userJson = JsonParser.parseString(body)

        if (userJson.isJsonObject) {
            val json = userJson.asJsonObject
            var fullName: String? = null;

            if (json.getAsJsonObject("subreddit").isJsonObject) {
                val title = json.getAsJsonObject("subreddit").getAsJsonPrimitive("title")
                fullName = if (title.isString && title.asString != "") title.asString else null;
            }
            val id = json.getAsJsonPrimitive("id").asString

            userDao.upsertUser(
                User(
                    id = id,
                    username = json.getAsJsonPrimitive("name").asString,
                    created = json.getAsJsonPrimitive("created").asLong,
                    karma = json.getAsJsonPrimitive("total_karma").asInt,
                    fullName = fullName,
                    profilePictureUrl = Jsoup.parse(json.getAsJsonPrimitive("icon_img").asString)
                        .text(),
                    accessToken = tokenRetrievalResponse.accessToken,
                    refreshToken = tokenRetrievalResponse.refreshToken,
                    tokenExpiration = Instant.now().epochSecond + tokenRetrievalResponse.expiresIn
                )
            )

            val sharedPrefs = application.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            with(sharedPrefs.edit()){
                putString("userId", id)
                commit()
            }

            val intent = application.packageManager.getLaunchIntentForPackage(application.packageName)
            application.startActivity(Intent.makeRestartActivityTask(intent?.component))
            Runtime.getRuntime().exit(0)
        } else {
            throw Exception("User JSON data is not an object")
        }
    }

    suspend fun exchangeCode(code: String) {
        val gson = Gson()
        val basicBytes = Base64.getEncoder().encode("$CLIENT_ID:".toByteArray())
        val basicEncodedString = String(basicBytes, charset = StandardCharsets.UTF_8)
        val basicAuth = "Basic $basicEncodedString"

        val params = mapOf<String, String>(
            "grant_type" to "authorization_code", "code" to code, "redirect_uri" to AUTH_REDIRECT
        ).map { (key, value) -> "$key=${encode(value)}" }.joinToString("&")


        val request = Request.Builder().url(AUTH_TOKEN_URL).addHeader("Authorization", basicAuth)
            .addHeader("Content-type", "application/x-www-form-urlencoded")
            .post(params.toRequestBody()).build()


        withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: ""

            if (body == "") {
                throw Exception("Body empty")
            }

            val data = gson.fromJson(body, TokenRetrievalResponse::class.java)
            saveUser(data)
        }
    }
}