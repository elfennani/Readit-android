package com.elfennani.readit.data.remote.dto

import com.elfennani.readit.domain.model.Image
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
    @SerializedName("sr_detail") val srDetails: SubredditDto,
    @SerializedName("gallery_data") val galleryData: GalleryDataDto?,
    @SerializedName("media_metadata") val mediaMetadata: Map<String, MediaMetadataDto>?,
    val preview: PostPreviewDto?
)

fun PostDto.toPost(): Post {
    val images: List<Image>? =
        when {
            galleryData != null && mediaMetadata != null ->
                galleryData.items.mapNotNull {
                    val data = mediaMetadata[it.mediaId]
                    if (data != null) {
                        return@mapNotNull Image(
                            caption =
                                if (it.caption != null) Jsoup.parse(it.caption).text() else null,
                            url = Jsoup.parse(data.s.u ?: data.s.gif ?: data.s.mp4 ?: "").text(),
                            width = data.s.x,
                            height = data.s.y
                        )
                    }

                    null
                }
            preview?.isPreviewEnabled == true ->
                preview.images.map {
                    Image(
                        url = Jsoup.parse(it.source.url).text(),
                        height = it.source.height,
                        width = it.source.width
                    )
                }
            else -> null
        }

    return Post(
        id = id,
        created = created,
        author = author,
        score = score,
        commentsSize = numComments,
        subreddit = subreddit,
        title = Jsoup.parse(title).text(),
        subredditDetails = srDetails.toSubreddit(),
        images = images
    )
}
