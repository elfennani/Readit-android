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

  private val authorization = "Basic ${encodeBase64("$CLIENT_ID:")}"

  private fun encodeBase64(value: String): String {
    val encoder = Base64.getEncoder()
    val encoded = encoder.encode(value.toByteArray())
    return String(encoded, StandardCharsets.UTF_8)
  }

  suspend fun addSession(code: String) {
    val retrievalResponse =
      authApi.retrieveAccessToken(
        authorization = authorization,
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

    updateCurrentUser(user.id)
  }

  private fun updateCurrentUser(userId: String){
    val prefs = context.getSharedPreferences("prefs", ComponentActivity.MODE_PRIVATE)

    with(prefs.edit()) {
      putString("userId", userId)
      commit()
    }
  }

  suspend fun refreshSession(userId: String) {
    val oldSession = sessionDao.getSessionByUserId(userId)
    val refreshTokenResponse =
      authApi.refreshAccessToken(
        authorization = authorization,
        refreshToken = oldSession.refreshToken,
      )

    sessionDao.upsertSession(
      oldSession.copy(
        accessToken = refreshTokenResponse.accessToken,
        expiration = Instant.now().epochSecond + refreshTokenResponse.expiresIn
      )
    )
  }
}
