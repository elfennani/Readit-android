package com.elfennani.readit.data.remote.dto

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class CommentTypeAdapter : JsonDeserializer<CommentWrapperDto> {
    private val gson = Gson()
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CommentWrapperDto {
        val data = json.asJsonObject
        val kind = data.getAsJsonPrimitive("kind").asString
        val commentData = data.getAsJsonObject("data")

        return if (kind == "t1") {
            CommentWrapperDto(
                comment = gson.fromJson(commentData, CommentDto::class.java),
                more = null
            )
        } else {
            CommentWrapperDto(null, more = gson.fromJson(commentData, MoreCommentsDto::class.java))
        }
    }
}