package com.elfennani.readit.domain.model

data class Post(
    val id: String,
    val created: Long,
    val author: String,
    val title: String,
    val subreddit: String,
    val score: Int,
    val commentsSize: Int,
    val subredditDetails: Subreddit
)
