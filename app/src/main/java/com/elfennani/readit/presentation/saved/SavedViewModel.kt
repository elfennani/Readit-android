package com.elfennani.readit.presentation.saved

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.readit.data.remote.OAuthApi
import com.elfennani.readit.data.remote.dto.toPost
import com.elfennani.readit.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val oAuthApi: OAuthApi,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val username:String
        get() {
            return savedStateHandle.get<String>("username") ?: ""
        }

    private val _feedState = MutableStateFlow<List<Post>>(emptyList())
    val feedState: StateFlow<List<Post>> = _feedState

    private var _isFetchingNextPage = mutableStateOf(false)
    val isFetchingNextPage: State<Boolean> = _isFetchingNextPage

    private var after: String? = null

    init {
        fetchNextPage()
    }

    fun fetchNextPage() {
        if (_isFetchingNextPage.value) return

        viewModelScope.launch {
            _isFetchingNextPage.value = true
            try {
                val feedResponse = oAuthApi.getSavedPosts(username, after)
                val feedMapped = feedResponse.data.children.map { it.data.toPost() }
                _feedState.value = _feedState.value + feedMapped
                after = feedResponse.data.after
            } finally {
                _isFetchingNextPage.value = false
            }
        }
    }
}