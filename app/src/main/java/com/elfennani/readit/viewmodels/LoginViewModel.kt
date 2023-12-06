package com.elfennani.readit.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.elfennani.readit.utilities.AUTH_REDIRECT
import com.elfennani.readit.utilities.AUTH_STATE
import com.elfennani.readit.utilities.AUTH_URL
import com.elfennani.readit.utilities.CLIENT_ID
import com.elfennani.readit.utilities.AUTH_SCOPES
import com.elfennani.readit.utilities.AUTH_TOKEN_URL
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.nio.charset.StandardCharsets
import java.util.Base64

//https://www.reddit.com/api/v1/authorize?client_id=jZxbCi8nroDGjr_X7ss5AQ&response_type=code&state=READIT1&redirect_uri=readit%3A%2F%2Fredirect&duration=permanent&scope=identity%2Cedit%2Cflair%2Chistory%2Cmodconfig%2Cmodflair%2Cmodlog%2Cmodposts%2Cmodwiki%2Cmysubreddits%2Cprivatemessages%2Cread%2Creport%2Csave%2Csubmit%2Csubscribe%2Cvote%2Cwikiedit%2Cwikiread

data class TokenRetrievalResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: String,
    @SerializedName("refresh_token") val refreshToken: String,
)

class LoginViewModel : ViewModel() {
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

    fun encode(value: String): String {
        return java.net.URLEncoder.encode(value, "UTF-8")
    }

    suspend fun exchangeCode(code: String) {
        val gson = Gson()
        val basicBytes = Base64.getEncoder().encode("$CLIENT_ID:".toByteArray())
        val basicEncodedString = String(basicBytes, charset = StandardCharsets.UTF_8)
        val basicAuth = "Basic $basicEncodedString"

        val params = mapOf<String, String>(
            "grant_type" to "authorization_code",
            "code" to code,
            "redirect_uri" to AUTH_REDIRECT
        )
            .map { (key, value) -> "$key=${encode(value)}" }
            .joinToString("&")


        val request = Request.Builder()
            .url(AUTH_TOKEN_URL)
            .addHeader("Authorization", basicAuth)
            .addHeader("Content-type", "application/x-www-form-urlencoded")
            .post(params.toRequestBody())
            .build()

        val client = OkHttpClient();
        withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: ""

            if(body == ""){
                throw Exception("Body empty")
            }

            val data = gson.fromJson(body, TokenRetrievalResponse::class.java)
        }
    }
}