package com.elfennani.readit.data.remote.dto

data class DataDto<T> (
    val kind: String,
    val data:T,
)