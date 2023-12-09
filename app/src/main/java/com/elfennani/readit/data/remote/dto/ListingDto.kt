package com.elfennani.readit.data.remote.dto

data class ListingDto<T> (
    val after:String?,
    val before: String?,
    val children: List<T>,
    val dist: Int
)