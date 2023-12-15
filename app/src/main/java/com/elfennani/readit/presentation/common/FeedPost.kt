package com.elfennani.readit.presentation.common

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.ModeComment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.elfennani.readit.domain.model.Gallery
import com.elfennani.readit.domain.model.Image
import com.elfennani.readit.domain.model.Post
import com.elfennani.readit.domain.model.Subreddit
import com.elfennani.readit.utilities.formatDifferenceSeconds
import com.ireward.htmlcompose.HtmlText
import java.time.Instant

@Composable
fun FeedPost(post: Post) {
    val timeDifference = remember {
        formatDifferenceSeconds(Instant.now().epochSecond - post.created)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Black, contentColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape
    ) {
        Row(Modifier.padding(8.dp)) {
            AsyncImage(
                post.subredditDetails.icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp).clip(CircleShape),
                placeholder =
                    BrushPainter(
                        Brush.linearGradient(
                            listOf(
                                Color(color = 0xFF505050),
                                Color(color = 0xFF3A3A3A),
                            )
                        )
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "r/${post.subreddit}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• $timeDifference ago",
                        fontSize = 10.sp,
                        modifier = Modifier.alpha(0.6f)
                    )
                }
                Text(text = post.title, lineHeight = 18.sp, fontSize = 14.sp)

                if(post.gallery.images != null){
                    Spacer(modifier = Modifier.height(8.dp))
                    if(post.gallery.images.size == 1){
                        AsyncImage(
                            model = post.gallery.images[0].url,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio((16).toFloat()/9)
                                .clip(RoundedCornerShape(12.dp)),
                            placeholder = ColorPainter(Color.DarkGray)
                        )
                    }
                }

                if(post.html != null){
                    HtmlText(text = post.html, style = TextStyle(color = Color.LightGray), maxLines = 3, overflow = TextOverflow.Ellipsis)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Row (horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconInfo(icon = Icons.Rounded.FavoriteBorder, text = post.score.toString())
                        IconInfo(icon = Icons.Rounded.ModeComment, text = post.commentsSize.toString())
                    }

                    IconInfo(icon = Icons.Rounded.BookmarkBorder)
                }
            }
        }
    }
}

@Composable
fun IconInfo(icon:ImageVector, text: String? = null, isActive: Boolean = false, activeIcon: ImageVector? = null){
    Row (verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, Modifier.size(16.dp))
        if(text != null){
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text, fontSize = 10.sp)
        }
    }
}

@Preview
@Composable
fun TextPost() {
    Column(Modifier.width(350.dp)) {
        FeedPost(
            post =
                Post(
                    title = "Is 4 year degree the only way to break into a programming career?",
                    video = null,
                    gallery = Gallery(null),
                    html =
                        "<!-- SC_OFF --><div class=\\\"md\\\"><p>Im a 28 year process tech. I’m thinking of a career change and am fascinated by software development. Can I be hired with only self taught resume and a stack GitHub repository? Or is a four year the only way? Is there any cases of people being hired without one?</p>\\n</div><!-- SC_ON -->",
                    isNsfw = false,
                    id = "18eym9h",
                    created = 1702194990L,
                    author = "ProgrammerNational49",
                    score = 123,
                    subredditDetails =
                        Subreddit(
                            id = null,
                            title = "learnprogramming",
                            color = Color.LightGray.toArgb(),
                            icon = null
                        ),
                    commentsSize = 12,
                    subreddit = "learnprogramming"
                )
        )
    }
}

@Preview
@Composable
fun SingleImagePost() {
    Column(Modifier.width(350.dp)) {
        FeedPost(
            post =
            Post(
                title = "Is 4 year degree the only way to break into a programming career?",
                video = null,
                gallery = Gallery(images = listOf(Image(
                    url = "https://images.unsplash.com/photo-1531500435567-d5a03bbb46a6?q=80&w=1957&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    width = 1957,
                    height = 1074
                ))),
                html = null,
                isNsfw = false,
                id = "18eym9h",
                created = 1702194990L,
                author = "ProgrammerNational49",
                score = 123,
                subredditDetails =
                Subreddit(
                    id = null,
                    title = "learnprogramming",
                    color = Color.LightGray.toArgb(),
                    icon = null
                ),
                commentsSize = 12,
                subreddit = "learnprogramming"
            )
        )
    }
}
