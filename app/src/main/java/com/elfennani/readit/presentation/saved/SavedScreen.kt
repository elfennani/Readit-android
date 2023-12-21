package com.elfennani.readit.presentation.saved

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elfennani.readit.presentation.common.PostListView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(navController: NavHostController, viewModel: SavedViewModel = hiltViewModel()) {
    val data = viewModel.feedState.collectAsState()
    val distinctFeed by remember { derivedStateOf { data.value.distinctBy { it.id } } }

    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                }
            },
            title = {
                Text(text = "Saved")
            })
    }) {
        PostListView(
            posts = distinctFeed,
            onFetchNextPage = { /*TODO*/ },
            isFetchingNextPage = viewModel.isFetchingNextPage.value,
            contentPadding = it,
            navController = navController
        )
    }
}