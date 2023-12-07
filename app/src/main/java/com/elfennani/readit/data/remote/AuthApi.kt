package com.elfennani.readit.data.remote

import com.elfennani.readit.data.remote.dto.RefreshTokenDto
import com.elfennani.readit.data.remote.dto.RetrieveTokenDto
import com.elfennani.readit.utilities.AUTH_REDIRECT
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
  @FormUrlEncoded
  @POST("/api/v1/access_token")
  suspend fun retrieveAccessToken(
    @Header("Authorization") authorization: String,
    @Field("grant_type") grantType: String = "authorization_code",
    @Field("code") code: String,
    @Field("redirect_uri") redirectUri: String = AUTH_REDIRECT
  ) : RetrieveTokenDto

  @FormUrlEncoded
  @POST("/api/v1/access_token")
  suspend fun refreshAccessToken(
    @Header("Authorization") authorization: String,
    @Field("grant_type") grantType: String = "refresh_token",
    @Field("refresh_token") refreshToken: String,
  ) : RefreshTokenDto
}
