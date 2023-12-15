package com.elfennani.readit.presentation.postscreen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.readit.presentation.homefeed.components.PostView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postScreenViewModel: PostScreenViewModel = hiltViewModel(),
    navController: NavController
) {
    val post by remember { postScreenViewModel.post }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    if (post != null) {
                        Text(
                            text = post!!.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    } else {
                        Text(text = "Post")
                    }
                }
            )
        }
    ) {
        LazyColumn(contentPadding = it) {
            post?.let { post -> item { PostView(post = post, compact = false) } }
        }
    }
}
