package com.elfennani.readit.presentation.homefeed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.elfennani.readit.domain.model.User
import com.elfennani.readit.utilities.formatDifferenceSeconds
import com.valentinilk.shimmer.shimmer

@Composable
fun UserProfile(user: User?) {
    if (user == null) {
        Column(
            modifier = Modifier.padding(24.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .shimmer()
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .shimmer()
                        .clip(CircleShape)
                        .background(Color.LightGray)

                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .width(170.dp)
                    .height(24.dp)
                    .shimmer()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(18.dp)
                    .shimmer()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .width(190.dp)
                    .height(18.dp)
                    .shimmer()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray)
            )
        }
    } else {
        Column(
            modifier = Modifier.padding(24.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = user.profile,
                    contentDescription = null,
                    modifier = Modifier
                        .width(56.dp)
                        .height(56.dp)
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
                Icon(
                    Icons.Rounded.Group,
                    contentDescription = null,
                    Modifier
                        .offset(x = 8.dp)
                        .clip(CircleShape)
                        .clickable { }
                        .padding(8.dp)
                        .size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (user.fullName != null) {
                Text(
                    text = user.fullName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = user.username,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                Text(
                    text = user.username,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                val labelText: (String, String) -> AnnotatedString = { amount, label ->
                    buildAnnotatedString {
                        append(amount)
                        append(" ")
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append(label)
                        }
                    }
                }
                Text(text = labelText(user.totalKarma.toString(), "karma"), fontSize = 14.sp)
                Text(text = labelText(formatDifferenceSeconds(user.age, true), "age"), fontSize = 14.sp)
            }
        }
    }
}

@Preview
@Composable
fun UserProfilePreview() {
    Column(
        Modifier
            .background(Color.White)
            .width(350.dp)
    ) {
        UserProfile(
            user = User(
                id = "",
                profile = "https://styles.redditmedia.com/t5_vjcux/styles/profileIcon_snoo4eb7f2fb-0e85-4c4d-8ec2-0ee989b23566-headshot-f.png?width=256&height=256&crop=256:256,smart&s=1e981ef29c72b2db0f07ad1f2e48b28b490d9f8f",
                fullName = "Nizar Elfennani",
                totalKarma = 23123,
                age = 153284409,
                username = "u/elfennani"
            )
        )
    }
}