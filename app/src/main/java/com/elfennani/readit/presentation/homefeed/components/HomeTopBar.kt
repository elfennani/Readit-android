package com.elfennani.readit.presentation.homefeed.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onSearch: () -> Unit,
    onDrawerOpen: () -> Unit,
) {
    TopAppBar(actions = {
        IconButton(onClick = onSearch) {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
    }, title = { Text(text = "Home") }, navigationIcon = {
        IconButton(onClick = onDrawerOpen) {
            Icon(Icons.Rounded.Menu, contentDescription = null)
        }
    })
}