package com.elfennani.readit.presentation.postscreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.readit.data.remote.OAuthApi
import com.elfennani.readit.data.remote.dto.CommentWrapperDto
import com.elfennani.readit.domain.model.Post
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class PostScreenViewModel
@Inject
constructor(private val oAuthApi: OAuthApi, private val savedStateHandle: SavedStateHandle) :
    ViewModel() {

    private val _post = mutableStateOf<Post?>(null)
    val post: State<Post?> = _post

    private val _comments = MutableStateFlow<List<CommentWrapperDto>>(emptyList())
    val comments : StateFlow<List<CommentWrapperDto>> = _comments

    init {
        savedStateHandle.get<String>("post").let {
            if (it != null) {
                val postDeserializer = Gson().fromJson(URLDecoder.decode(it), Post::class.java)
                _post.value = postDeserializer
            } else {
                Log.d("POSTSCREEN", "null")
            }
        }

        savedStateHandle.get<String>("id").let { fetchData(checkNotNull(it)) }
    }

    private fun fetchData(id: String) {
        viewModelScope.launch {
            val data = oAuthApi.getPostComments(id)
            Log.d("POSTSCREEN", data.comments.data.children.toString())
            _comments.emit(data.comments.data.children)
        }
    }
}
