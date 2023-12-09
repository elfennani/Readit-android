package com.elfennani.readit.domain.model

data class Image(
    val url: String,
    val width: Int,
    val height: Int,
    val caption: String? = null
)
