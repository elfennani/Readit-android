package com.elfennani.readit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session (
    @PrimaryKey val userId: String,
    val username: String,
    val accessToken: String,
    val refreshToken: String,
    val expiration: Long,
)