package com.elfennani.readit.domain.model

data class Post(
    val id: String,
    val created: Long,
    val author: String,
    val title: String,
    val subreddit: String,
    val score: Int,
    val commentsSize: Int,
    val subredditDetails: Subreddit,
    val html: String?,
    val md: String?,
    val gallery: Gallery,
    val video: Video? = null,
    val isNsfw : Boolean = false,
    val crosspost: Post?
)
