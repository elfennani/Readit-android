package com.elfennani.readit.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import com.elfennani.readit.data.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeFeedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userDao: UserDao,
) : ViewModel() {
    private val userId  =  checkNotNull(savedStateHandle.get<String>("userId"))
    val user = userDao.getUserById(userId)
    init {
        Log.d("USERIDINIT", userId.toString())
    }
}