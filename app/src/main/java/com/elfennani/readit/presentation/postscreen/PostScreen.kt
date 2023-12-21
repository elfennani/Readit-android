package com.elfennani.readit.presentation.postscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.readit.presentation.common.PostView
import org.commonmark.ext.autolink.AutolinkExtension
import org.commonmark.node.Document
import org.commonmark.parser.Parser
import se.hellsoft.markdowncomposer.MDDocument

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postScreenViewModel: PostScreenViewModel = hiltViewModel(), navController: NavController
) {
    val post by remember { postScreenViewModel.post }
    val comments by postScreenViewModel.comments.collectAsState()


    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = null)
            }
        }, title = {
            if (post != null) {
                Text(
                    text = post!!.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            } else {
                Text(text = "Post")
            }
        })
    }) {
        LazyColumn(contentPadding = it) {
            post?.let { post ->
                item {
                    PostView(post = post, compact = false)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            items(comments,
                key = { comment -> comment.comment?.id ?: comment.more?.id ?: "" }) { comment ->
                val depth = comment.comment?.depth ?: comment.more?.depth ?: 0
                Card(
                    Modifier
                        .padding(top = if (depth == 0) 8.dp else 0.dp)
                        .fillMaxWidth(), shape = RectangleShape
                ) {
                    Row(
                        Modifier
                            .height(IntrinsicSize.Min)
                            .padding(start = (depth * 16).dp)
                            .padding(vertical = 4.dp)
                    ) {
                        if (depth >= 1) Divider(
                            modifier = Modifier
                                .fillMaxHeight()  //important
                                .width(1.dp)
                        )
                        if (comment.comment != null) {
                            Column(
                                modifier = Modifier.padding(16.dp, 4.dp)
                            ) {
                                Text(
                                    "u/${comment.comment.author}",
                                    fontSize = 12.sp,
                                    modifier = Modifier.alpha(0.6f)
                                )
                                MDDocument(
                                    document = Parser.Builder()
                                        .extensions(listOf(AutolinkExtension.create())).build()
                                        .parse(comment.comment.body) as Document
                                )
                            }
                        } else if (comment.more != null) {
                            Column(Modifier.padding(16.dp, 4.dp)) {
                                TextButton(onClick = { /*TODO*/ }) {
                                    Text(text = "More Comments: ${comment.more.children.size}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun test() {
//    val ar: Array<{ Int & String }> = arrayOf(5, "hello", 5)
}