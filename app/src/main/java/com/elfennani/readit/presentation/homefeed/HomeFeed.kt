package com.elfennani.readit.presentation.homefeed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.readit.presentation.homefeed.components.PostView
import com.elfennani.readit.presentation.homefeed.components.UserProfile
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFeed(
    navController: NavController,
    userId: String,
    homeFeedViewModel: HomeFeedViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val state = homeFeedViewModel.userState
    val data = homeFeedViewModel.feedState.collectAsState()
    val distinctFeed by remember {
        derivedStateOf {
            data.value.distinctBy { it.id }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                val user = state.value.data

                UserProfile(user = user)
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    },
                    title = { Text(text = "Home") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Rounded.Menu, contentDescription = null)
                        }
                    }
                )
            }
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp), contentPadding = it) {
                itemsIndexed(distinctFeed, key = { _, post -> post.id }) { index, post ->
                    PostView(post = post)

                    if (index >= data.value.size - 2) {
                        homeFeedViewModel.fetchNextPage()
                    }
                }
            }
        }
    }
}
