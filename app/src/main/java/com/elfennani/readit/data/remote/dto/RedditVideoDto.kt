package com.elfennani.readit.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RedditVideoDto(
    @SerializedName("hls_url") val hlsUrl: String,
    val width: Int,
    val height: Int
)
