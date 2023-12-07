package com.elfennani.readit.data.remote.dto


import com.google.gson.annotations.SerializedName

data class RefreshTokenDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Long,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("token_type")
    val tokenType: String
)