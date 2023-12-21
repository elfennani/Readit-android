package com.elfennani.readit.data.remote

import com.elfennani.readit.data.remote.dto.DataDto
import com.elfennani.readit.data.remote.dto.ListingDto
import com.elfennani.readit.data.remote.dto.PostDetailsDto
import com.elfennani.readit.data.remote.dto.PostDto
import com.elfennani.readit.data.remote.dto.SubredditDto
import com.elfennani.readit.data.remote.dto.user_api.ApiUser
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class Comments(
    val post: DataDto<ListingDto<PostDto>>,
    val comments: DataDto<ListingDto<Any>>
)

interface OAuthApi {
    @GET("/api/v1/me") suspend fun getIdentity(): ApiUser

    @GET("/best?sr_detail=true")
    suspend fun getBestFeed(
        @Query("after") after: String? = null
    ): DataDto<ListingDto<DataDto<PostDto>>>

    @GET("/r/{subreddit}/about")
    suspend fun getSubreddit(@Path("subreddit") subreddit: String): DataDto<SubredditDto>

    @GET("/comments/{id}?threaded=false")
    suspend fun  getPostComments(@Path("id") id:String) : PostDetailsDto
}
