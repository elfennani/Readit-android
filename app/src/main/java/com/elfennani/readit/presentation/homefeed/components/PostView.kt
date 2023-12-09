package com.elfennani.readit.presentation.homefeed.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.elfennani.readit.domain.model.Post
import com.elfennani.readit.domain.model.Subreddit
import com.elfennani.readit.utilities.formatDifferenceSeconds
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostView(post: Post, onPostPress: ((Post) -> Unit)? = null) {
    val age by
        remember(key1 = post.id) {
            derivedStateOf { formatDifferenceSeconds(Instant.now().epochSecond - post.created) }
        }
    val subIconUrl = post.subredditDetails.icon

    Card(
        shape = RectangleShape,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        onClick = { onPostPress?.invoke(post) }
    ) {
        Column(Modifier.padding(0.dp, 16.dp)) {
            Row(
                modifier = Modifier.padding(16.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (subIconUrl == null || subIconUrl == "") {
                    Box(modifier = Modifier.size(32.dp))
                } else {
                    AsyncImage(
                        model = subIconUrl,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp).clip(CircleShape),
                        placeholder =
                            BrushPainter(
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
                        "r/${post.author} • $age",
                        fontSize = 12.sp,
                        modifier = Modifier.alpha(0.6f)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = post.title,
                modifier = Modifier.padding(16.dp, 0.dp),
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(12.dp))

            if (!post.images.isNullOrEmpty()) {
                val firstImage = post.images[0]

                AsyncImage(
                    model = firstImage.url,
                    contentDescription = null,
                    modifier =
                        Modifier.fillMaxWidth().aspectRatio(firstImage.width.toFloat() / firstImage.height)
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
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
    Row(
        modifier =
            Modifier.border(
                    width = 1.dp,
                    shape = RoundedCornerShape(100.dp),
                    color = Color(0xFF303030)
                )
                .clip(RoundedCornerShape(100.dp))
                .clickable {}
                .height(32.dp)
                .padding(12.dp, 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        content()
    }
}

@Preview
@Composable
fun PostViewPreview() {
    Column(modifier = Modifier.width(450.dp)) {
        PostView(
            post =
                Post(
                    title =
                        "I built a free LinkTree alternative that detects where a click comes from to instantly switch to the style of your social media profile.",
                    subreddit = "SideProject",
                    commentsSize = 12,
                    score = 125,
                    author = "FakeDreamsFakeHope",
                    id = "18cljw6",
                    created = 1701917350,
                    subredditDetails = Subreddit(id = "", title = "SideProject", icon = null)
                ),
        )
    }
}
