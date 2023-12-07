package com.elfennani.readit.data.repository

import android.content.Context
import androidx.activity.ComponentActivity
import com.elfennani.readit.data.local.Session
import com.elfennani.readit.data.local.SessionDao
import com.elfennani.readit.data.remote.AuthApi
import com.elfennani.readit.data.remote.UserApi
import com.elfennani.readit.utilities.CLIENT_ID
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Base64
import javax.inject.Inject

class SessionManager
@Inject
constructor(
  private val context: Context,
  private val authApi: AuthApi,
  private val userApi: UserApi,
  private val sessionDao: SessionDao
) {
  private fun encodeBase64(value: String): String {
    val encoder = Base64.getEncoder()
    val encoded = encoder.encode(value.toByteArray())
    return String(encoded, StandardCharsets.UTF_8)
  }

  suspend fun addSession(code: String) {
    val retrievalResponse =
      authApi.retrieveAccessToken(
        authorization = "Basic ${encodeBase64("$CLIENT_ID:")}",
        code = code,
      )

    val user = userApi.getUser("Bearer ${retrievalResponse.accessToken}")

    sessionDao.upsertSession(
      Session(
        username = user.name,
        accessToken = retrievalResponse.accessToken,
        refreshToken = retrievalResponse.refreshToken,
        expiration = Instant.now().epochSecond + retrievalResponse.expiresIn,
        userId = user.id
      )
    )

    val prefs = context.getSharedPreferences("prefs", ComponentActivity.MODE_PRIVATE)

    with(prefs.edit()) {
      putString("userId", user.id)
      commit()
    }
  }
}
