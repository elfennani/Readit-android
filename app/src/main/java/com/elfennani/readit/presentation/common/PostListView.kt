package com.elfennani.readit.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elfennani.readit.domain.model.Post
import com.elfennani.readit.presentation.Screen
import com.google.gson.Gson
import java.net.URLEncoder

@Composable
fun PostListView(
    posts: List<Post>,
    onFetchNextPage: () -> Unit,
    isFetchingNextPage: Boolean,
    contentPadding: PaddingValues,
    navController: NavController
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .collect {
                if (it >= posts.size - 7) {
                    onFetchNextPage()
                }
            }
    }

    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        items(posts, key = { post -> post.id }) { post ->
            PostView(
                post = post,
                onPostPress = {
                    navController.navigate(
                        Screen.PostScreenWithInitialData.createRoute(
                            it.id,
                            URLEncoder.encode(Gson().toJson(it, Post::class.java))
                        )
                    )
                },
                onImageOpen = {
                    navController.navigate(Screen.GalleryScreen.createRoute(it))
                },
            )
        }
        if (isFetchingNextPage) {
            item {
                Column(
                    Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

}