package com.elfennani.readit.data.remote

import com.elfennani.readit.data.remote.dto.user_api.AuthUserMe
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {
    @GET("/api/v1/me")
    suspend fun getUser(
        @Header("Authorization") authorization: String,
    ) : AuthUserMe
}