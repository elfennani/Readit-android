package com.elfennani.readit.data.remote.dto

import com.elfennani.readit.domain.model.Subreddit
import com.google.gson.annotations.SerializedName
import org.jsoup.Jsoup

fun SubredditDto.toSubreddit(): Subreddit {
    val communityIcon = Jsoup.parse(communityIcon ?: "").text()
    val iconImg = Jsoup.parse(iconImg).text()
    val icon =
        when {
            communityIcon != "" -> communityIcon
            iconImg != "" -> iconImg
            else -> null
        }

    return Subreddit(
        id = id ?: name.replace("t5_", ""),
        title = title,
        icon = icon,
    )
}

data class SubredditDto(
    val id: String?,
    val name: String,
    val title: String,
    @SerializedName("icon_img") val iconImg: String,
    @SerializedName("community_icon") val communityIcon: String?
)
