package com.elfennani.readit.data.remote.dto

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class PostAdapter : TypeAdapter<PostDetailsDto>() {
    private val gson = GsonBuilder()
        .registerTypeAdapter(CommentWrapperDto::class.java, CommentTypeAdapter())
        .create()

    override fun read(reader: JsonReader): PostDetailsDto {
        reader.beginArray()
        val postType = object : TypeToken<DataDto<ListingDto<DataDto<PostDto>>>>() {}.type
        val post = gson.fromJson<DataDto<ListingDto<DataDto<PostDto>>>>(reader, postType)
        val commentsType = object : TypeToken<DataDto<ListingDto<CommentWrapperDto>>>() {}.type
        val comments = gson.fromJson<DataDto<ListingDto<CommentWrapperDto>>>(reader, commentsType)
        reader.endArray()
        return PostDetailsDto(post, comments)
    }

    override fun write(out: JsonWriter?, value: PostDetailsDto?) {
        TODO("Not yet implemented")
    }
}