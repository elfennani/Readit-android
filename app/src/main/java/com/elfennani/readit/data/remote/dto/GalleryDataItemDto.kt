package com.elfennani.readit.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GalleryDataItemDto(
    @SerializedName("media_id") val mediaId:String,
    val id: String,
    val caption: String?
)
