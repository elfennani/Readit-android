package com.elfennani.readit.presentation.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.readit.common.ResourceDataLess
import com.elfennani.readit.data.repository.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(
  private val sessionManager: SessionManager,
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {
  private val _state = mutableStateOf<ResourceDataLess>(ResourceDataLess.Loading)
  val state : State<ResourceDataLess> = _state
  init {
    savedStateHandle.get<String>("code")?.let { code ->
      viewModelScope.launch {
        try {
        _state.value = ResourceDataLess.Loading
        sessionManager.addSession(code)
        _state.value = ResourceDataLess.Success
        }catch (e: HttpException){
          _state.value = ResourceDataLess.Error(e.message())
        }catch (e:Exception) {
          _state.value = ResourceDataLess.Error("Something unexpected occurred")
        }
      }
    }
  }
}
