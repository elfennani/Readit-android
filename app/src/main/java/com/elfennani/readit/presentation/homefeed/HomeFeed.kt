package com.elfennani.readit.presentation.homefeed

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.elfennani.readit.presentation.Screen
import com.elfennani.readit.presentation.common.PostListView
import com.elfennani.readit.presentation.homefeed.components.HomeTopBar
import com.elfennani.readit.presentation.homefeed.components.UserProfile
import kotlinx.coroutines.launch

@Composable
fun HomeFeed(
    navController: NavController,
    homeFeedViewModel: HomeFeedViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val state = homeFeedViewModel.userState
    val data = homeFeedViewModel.feedState.collectAsState()
    val distinctFeed by remember { derivedStateOf { data.value.distinctBy { it.id } } }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
            val user = state.value.data
            UserProfile(user = user)
            if (user != null)
                NavigationDrawerItem(
                    modifier = Modifier.padding(4.dp, 0.dp),
                    icon = {
                        Icon(Icons.Rounded.Bookmark, contentDescription = null)
                    },
                    label = {
                        Text(text = "Saved")
                    },
                    selected = false,
                    onClick = {
                        navController.navigate(
                            Screen.SavedScreen.route.replace(
                                "{username}",
                                user.username.replaceFirst("u/","")
                            )
                        )
                    }
                )
        }
    }) {
        Scaffold(topBar = {
            HomeTopBar(onSearch = { /*TODO*/ }, onDrawerOpen = {
                scope.launch { drawerState.open() }
            })
        }) {
            PostListView(
                posts = distinctFeed,
                onFetchNextPage = { homeFeedViewModel.fetchNextPage() },
                isFetchingNextPage = homeFeedViewModel.isFetchingNextPage.value,
                contentPadding = it,
                navController = navController
            )
        }
    }
}
