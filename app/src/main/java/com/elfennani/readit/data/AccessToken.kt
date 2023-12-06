package com.elfennani.readit.data

import java.util.Date

/*
    "access_token": Your access token,
    "token_type": "bearer",
    "expires_in": Unix Epoch Seconds,
    "scope": A scope string,
    "refresh_token": Your refresh token
 */
data class AccessToken(
    val accessToken: String,
    val expiresIn: Date,
    val refreshToken: String
)
