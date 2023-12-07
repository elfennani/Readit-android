package com.elfennani.readit.data.remote.dto.user_api


import com.google.gson.annotations.SerializedName

data class MwebNsfwXpromo(
    @SerializedName("experiment_id")
    val experimentId: Int,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("variant")
    val variant: String
)