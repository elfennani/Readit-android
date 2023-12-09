package com.elfennani.readit.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PostPreviewDto (
    @SerializedName("enabled") val isPreviewEnabled: Boolean,
    val images: List<PreviewImageDto>
)