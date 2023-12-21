package com.elfennani.readit.data.remote.dto

data class PostDetailsDto(
    val post: DataDto<
            ListingDto<DataDto<PostDto>>>,
    val comments: DataDto<ListingDto<CommentWrapperDto>>
)
