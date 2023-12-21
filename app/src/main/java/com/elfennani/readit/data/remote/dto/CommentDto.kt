package com.elfennani.readit.data.remote.dto

data class CommentDto(
    val author:String,
    val body: String,
    val depth: Int,
    val score:Int,
    val id: String
)
