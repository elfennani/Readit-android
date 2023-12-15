package com.elfennani.readit.presentation.postscreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.elfennani.readit.data.remote.OAuthApi
import com.elfennani.readit.domain.model.Post
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class PostScreenViewModel
@Inject
constructor(private val oAuthApi: OAuthApi, private val savedStateHandle: SavedStateHandle) :
    ViewModel() {

    private val _post = mutableStateOf<Post?>(null)
    val post: State<Post?> = _post

    init {
        savedStateHandle.get<String>("post").let {
            if (it != null) {
                val postDeserializer = Gson().fromJson(URLDecoder.decode(it), Post::class.java)
                _post.value = postDeserializer
            } else {
                Log.d("POSTSCREEN", "null")
            }
        }

        savedStateHandle.get<String>("id").let { Log.d("POSTSCREENID", it.toString()) }
    }
}
