package com.elfennani.readit.presentation.homefeed

import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
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
    val markdown = """
# Heading 1 
## Heading 2
### Heading 3
#### Heading 4
##### Heading 5
###### Heading 6

this is a paragraph, it contains *italic*, **bold**, ***Bold Italic***, ~~strikethrough~~, >!spoiler!<, `code`, and ^superscript text

and [this link](https://redditinc.com) leads to reddit

- List 1
- List 2 **with bold text**

1. what about numbered lists
2. or ordered in other words
    1. what about indented list?
    
> What about blockquotes
> Working as intended?

> Yep!

```
@Composable
fun RenderMarkdown(){
    Text(text= "hello world")
}
```

hi *there* [link](https://google.com)
    """.trimIndent()


    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
            val user = state.value.data
            UserProfile(user = user)
        }
    }) {
        Scaffold(topBar = {
            HomeTopBar(onSearch = { /*TODO*/ }, onDrawerOpen = {
                scope.launch { drawerState.open() }
            })
        }) {
//            Column(
//                Modifier
//                    .padding(it)) {
//                MDDocument(Parser.Builder().build().parse(markdown) as Document)
//            }
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
