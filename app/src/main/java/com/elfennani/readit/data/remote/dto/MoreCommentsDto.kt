package com.elfennani.readit.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MoreCommentsDto(
    val count: Int,
    val depth: Int,
    val children: List<String>,
    val name: String,
    val id: String,
    @SerializedName("parent_id") val parentId: String
)