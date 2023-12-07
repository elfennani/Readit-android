package com.elfennani.readit.data.remote

import com.elfennani.readit.data.remote.dto.user_api.ApiUser
import retrofit2.http.GET

interface OAuthApi {
    @GET("/api/v1/me")
    suspend fun getIdentity() : ApiUser
}