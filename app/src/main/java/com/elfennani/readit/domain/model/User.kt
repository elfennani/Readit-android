package com.elfennani.readit.domain.model

data class User(
    val username: String,
    val fullName: String?,
    val totalKarma: Int,
    val age: Long,
    val id: String,
    val profile: String,
)
