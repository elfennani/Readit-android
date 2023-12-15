package com.elfennani.readit.presentation.homefeed.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.elfennani.readit.domain.model.Gallery
import com.elfennani.readit.domain.model.Post
import com.elfennani.readit.domain.model.Subreddit
import com.elfennani.readit.utilities.formatDifferenceSeconds
import com.ireward.htmlcompose.HtmlText
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PostView(
    post: Post,
    compact: Boolean = true,
    onPostPress: ((Post) -> Unit)? = null,
    onImageOpen: ((Gallery) -> Unit)? = null
) {
    val age by remember(key1 = post.id) {
        derivedStateOf { formatDifferenceSeconds(Instant.now().epochSecond - post.created) }
    }
    val subIconUrl = post.subredditDetails.icon
    val pagerState = rememberPagerState(pageCount = { post.gallery.images?.size ?: 0 })
    var shouldHide by remember { mutableStateOf(post.isNsfw) }
    var shouldPlay by remember { mutableStateOf(false) }

    Card(shape = RectangleShape,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        onClick = { onPostPress?.invoke(post) }) {
        Column(Modifier.padding(0.dp, 16.dp)) {
            Row(
                modifier = Modifier.padding(16.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (subIconUrl == null || subIconUrl == "") {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(
                                Color(post.subredditDetails.color ?: Color.Blue.toArgb())
                            )
                            .size(32.dp), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "r/",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                } else {
                    AsyncImage(
                        model = subIconUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        placeholder = BrushPainter(
                            Brush.linearGradient(
                                listOf(
                                    Color(color = 0xFFFFFFFF),
                                    Color(color = 0xFFDDDDDD),
                                )
                            )
                        ),
                    )
                }
                Column {
                    Text("r/${post.subreddit}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(
                        "r/${post.author} • $age", fontSize = 12.sp, modifier = Modifier.alpha(0.6f)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            if (post.isNsfw) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(16.dp, 4.dp)
                ) {
                    Icon(
                        Icons.Rounded.Info,
                        contentDescription = null,
                        Modifier.size(14.dp),
                        tint = Color.Red
                    )
                    Text(text = "NSFW", fontSize = 12.sp, color = Color.Red)
                }
            }
            Text(
                text = post.title,
                modifier = Modifier.padding(16.dp, 0.dp),
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(12.dp))

            if (shouldHide && (post.video != null || post.gallery.images != null)) {
                TextButton(modifier = Modifier.fillMaxWidth(), onClick = { shouldHide = false }) {
                    Text("Show NSFW")
                }
            }

            if (post.video != null && !shouldHide) {
                val context = LocalContext.current
                val exoPlayer = remember {
                    ExoPlayer.Builder(context).build().apply {
                        setMediaItem(MediaItem.fromUri(post.video.url))
                        prepare()
                    }
                }

                DisposableEffect(key1 = exoPlayer) { onDispose { exoPlayer.release() } }

                if (shouldPlay) {

                    AndroidView(
                        factory = { PlayerView(it).apply { player = exoPlayer } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(post.video.width.toFloat() / post.video.height)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(post.video.width.toFloat() / post.video.height)
                            .clickable {
                                shouldPlay = true
                                exoPlayer.play()

                            }, contentAlignment = Alignment.Center
                    ) {
                        if (post.video.thumbnail != null) {
                            AsyncImage(
                                model = post.video.thumbnail,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(
                                        post.video.width.toFloat() / post.video.height
                                    ),
                            )
                        }

                        Icon(
                            Icons.Rounded.PlayCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color.Black, CircleShape)
                        )
                    }
                }
            }

            if (!post.gallery.images.isNullOrEmpty() && !shouldHide) {
                Box(modifier = Modifier.clickable { onImageOpen?.invoke(post.gallery) }) {
                    if (post.gallery.images.size > 1) {
                        HorizontalPager(state = pagerState) {
                            val image = post.gallery.images[it]

                            AsyncImage(
                                model = image.url,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(image.width.toFloat() / image.height)
                            )
                        }
                    } else {
                        val firstImage = post.gallery.images[0]

                        AsyncImage(
                            model = firstImage.url,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(firstImage.width.toFloat() / firstImage.height)
                        )
                    }
                }
                //                Spacer(Modifier.height(12.dp))
            }

            if (!post.html.isNullOrEmpty()) {
                if (compact) {
                    HtmlText(
                        text = post.html,
                        modifier = Modifier.padding(16.dp, 0.dp),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp
                        ),
                        maxLines = 3
                    )
                } else {
                    HtmlText(
                        text = post.html,
                        modifier = Modifier.padding(16.dp, 0.dp),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp
                        ),
                        linkClicked = { Log.d("LINKCLICKED", it) },
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.padding(16.dp, 0.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RoundedInfo {
                    Icon(
                        Icons.Rounded.KeyboardArrowUp,
                        contentDescription = null,
                        Modifier.size(18.dp)
                    )
                    Text(text = "${post.score}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Icon(
                        Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null,
                        Modifier.size(18.dp)
                    )
                }
                RoundedInfo {
                    Icon(Icons.Rounded.Message, contentDescription = null, Modifier.size(16.dp))
                    Text(
                        text = "${post.commentsSize} comment",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun RoundedInfo(content: @Composable () -> Unit) {
    Row(modifier = Modifier
        .border(
            width = 1.dp, shape = RoundedCornerShape(100.dp), color = Color(0xFF303030)
        )
        .clip(RoundedCornerShape(100.dp))
        .clickable {}
        .height(32.dp)
        .padding(12.dp, 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        content()
    }
}

@Preview
@Composable
fun PostViewPreview() {
    Column(modifier = Modifier.width(450.dp)) {
        PostView(
            post = Post(
                title = "I built a free LinkTree alternative that detects where a click comes from to instantly switch to the style of your social media profile.",
                subreddit = "SideProject",
                commentsSize = 12,
                score = 125,
                author = "FakeDreamsFakeHope",
                id = "18cljw6",
                created = 1701917350,
                html = null,
                gallery = Gallery(),
                subredditDetails = Subreddit(
                    id = "",
                    title = "SideProject",
                    icon = null,
                    color = null
                )
            ),
        )
    }
}
