package com.elfennani.readit.data.remote.dto

import com.elfennani.readit.domain.model.Gallery
import com.elfennani.readit.domain.model.Image
import com.elfennani.readit.domain.model.Post
import com.elfennani.readit.domain.model.Video
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
    @SerializedName("selftext_html") val selftextHtml: String?,
    @SerializedName("is_video") val isVideo: Boolean,
    @SerializedName("over_18") val over18: Boolean,
    @SerializedName("crosspost_parent_list") val crosspost: List<PostDto>?,
    val media: PostMediaDto?,
    val preview: PostPreviewDto?,
    val selftext: String?,
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

    val thumbnail =
        when {
            preview?.images?.get(0)?.source?.url != null ->
                Jsoup.parse(preview.images[0].source.url).text()
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
        gallery = Gallery(images),
        html = if (selftextHtml != null) Jsoup.parse(selftextHtml).text() else null,
        isNsfw = over18,
        crosspost = if(!crosspost.isNullOrEmpty()) crosspost[0].toPost() else null,
        md = selftext,
        video =
            if (isVideo && media != null)
                Video(
                    url = Jsoup.parse(media.redditVideo.hlsUrl).text(),
                    width = media.redditVideo.width,
                    height = media.redditVideo.height,
                    thumbnail = thumbnail
                )
            else null
    )
}
