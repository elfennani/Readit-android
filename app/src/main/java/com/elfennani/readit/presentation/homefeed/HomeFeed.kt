package com.elfennani.readit.presentation.homefeed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elfennani.readit.presentation.homefeed.components.UserProfile
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFeed(
  navController: NavController,
  userId: String
) {
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
        val user = null //homeFeedViewModel.user.collectAsState(initial = null).value
        UserProfile(user = user)
      }
    }
  ) {
    Scaffold(
      topBar = {
        TopAppBar(
          actions = {
            IconButton(onClick = { /*TODO*/}) {
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
      Column(modifier = Modifier.padding(it)) { Text(text = "to be implemented $userId") }
    }
  }
}
