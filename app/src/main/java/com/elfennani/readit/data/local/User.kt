package com.elfennani.readit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("users")
data class User(
    @PrimaryKey
    val id:  String,
    val username: String,
    val accessToken: String,
    val refreshToken: String,
    val tokenExpiration: Long,
    val profilePictureUrl: String,
    val karma: Int,
    val created: Long,
    val fullName: String?
)
