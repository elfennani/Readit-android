package com.elfennani.readit.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PostMediaDto (
    @SerializedName("reddit_video") val redditVideo:RedditVideoDto
)