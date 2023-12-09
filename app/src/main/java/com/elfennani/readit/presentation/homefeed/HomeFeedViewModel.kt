package com.elfennani.readit.presentation.homefeed

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.readit.common.Resource
import com.elfennani.readit.data.remote.OAuthApi
import com.elfennani.readit.data.remote.dto.toPost
import com.elfennani.readit.data.remote.dto.user_api.toUser
import com.elfennani.readit.domain.model.Post
import com.elfennani.readit.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFeedViewModel
@Inject
constructor(private val oAuthApi: OAuthApi, private val savedStateHandle: SavedStateHandle) :
    ViewModel() {
    private val _userState = mutableStateOf<Resource<User>>(Resource.Loading())
    val userState: State<Resource<User>> = _userState

    private val _feedState = MutableStateFlow<List<Post>>(emptyList())
    val feedState: StateFlow<List<Post>> = _feedState

    private var isFetchingNextPage = false

    private var after: String? = null

    init {
        savedStateHandle.get<String>("userId")?.let { loadUser(it) }
        fetchNextPage()
    }

    fun fetchNextPage() {
        if (isFetchingNextPage) return
        isFetchingNextPage = true

        viewModelScope.launch {
            try {
                val feedResponse = oAuthApi.getBestFeed(after)
                val feedMapped = feedResponse.data.children.map { it.data.toPost() }
                _feedState.value = _feedState.value + feedMapped
                after = feedResponse.data.after
            } finally {
                isFetchingNextPage = false
            }
        }
    }

    private fun loadUser(userId: String) {
        viewModelScope.launch {
            try {
                _userState.value = Resource.Loading()
                _userState.value = Resource.Success(data = oAuthApi.getIdentity().toUser())
            } catch (e: Exception) {
                _userState.value = Resource.Error(message = "Something wrong occurred")
            }
        }
    }
}
