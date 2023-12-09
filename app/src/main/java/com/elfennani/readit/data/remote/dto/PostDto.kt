package com.elfennani.readit.data.remote.dto

import com.elfennani.readit.domain.model.Post
import com.google.gson.annotations.SerializedName
import org.jsoup.Jsoup

data class PostDto(
  val id: String,
  val created: Long,
  val author: String,
  val title: String,
  val subreddit: String,
  val score: Int,
  @SerializedName("num_comments") val numComments: Int,
  @SerializedName("sr_detail") val srDetails: SubredditDto
)

fun PostDto.toPost():Post{
  return Post(
    id = id,
    created = created,
    author = author,
    score = score,
    commentsSize = numComments,
    subreddit = subreddit,
    title = Jsoup.parse(title).text(),
    subredditDetails = srDetails.toSubreddit()
  )
}