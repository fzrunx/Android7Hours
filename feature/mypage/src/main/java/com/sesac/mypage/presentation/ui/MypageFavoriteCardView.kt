package com.sesac.mypage.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.sesac.common.R
import com.sesac.common.ui.theme.Error
import com.sesac.common.ui.theme.OnPrimaryContainer
import com.sesac.common.ui.theme.Primary
import com.sesac.common.ui.theme.Surface
import com.sesac.common.ui.theme.TextDisabled
import com.sesac.common.ui.theme.TextPrimary
import com.sesac.common.ui.theme.TextSecondary
import com.sesac.common.ui.theme.avatarSize
import com.sesac.common.ui.theme.cardImageSize
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.common.ui.theme.postImageHeight
import com.sesac.common.ui.theme.primaryContainer
import com.sesac.common.ui.theme.shapeCard
import com.sesac.common.ui.theme.shapeImage
import com.sesac.common.ui.theme.star
import com.sesac.domain.model.FavoriteCommunityPost
import com.sesac.domain.model.FavoriteWalkPath

@Composable
fun FavoriteWalkPathCard(
    path: FavoriteWalkPath,
//    viewModel: MypageViewModel,
    onPathClick: () -> Unit,
    onRemoveClick: (FavoriteWalkPath) -> Unit
) {
    Card(
        onClick = onPathClick,
        shape = shapeCard,
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(cardImageSize),
                contentAlignment = Alignment.TopEnd
            ) {
                AsyncImage(
                    model = path.image,
                    contentDescription = path.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shapeImage),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder)
                )
                Box(
                    modifier = Modifier
                        .padding(paddingMicro)
                        .size(24.dp)
                        .background(Surface.copy(alpha = 0.9f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Favorite",
                        tint = star,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(paddingMedium))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = path.name,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(paddingMicro))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = TextDisabled,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(paddingMicro))
                    Text(
                        text = path.location,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(paddingSmall))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        path.distance,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    Spacer(modifier = Modifier.width(paddingSmall))
                    Text(
                        "★ ${path.rating}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = star
                    )
                }
            }
            IconButton(
//                onClick = { viewModel.deleteFavoriteWalkPath(path) },
                onClick = { onRemoveClick(path) },
                modifier = Modifier.align(Alignment.Top)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "즐겨찾기에서 제거",
                    tint = Error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteCommunityPostCard(
    post: FavoriteCommunityPost,
//    viewModel: MypageViewModel,
    onPostClick: () -> Unit,
    onRemoveClick: (post: FavoriteCommunityPost) -> Unit
) {
    Card(
        onClick = onPostClick,
        shape = shapeCard,
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(paddingMedium)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = post.authorImage,
                    contentDescription = post.author,
                    modifier = Modifier
                        .size(avatarSize)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder)
                )
                Spacer(modifier = Modifier.width(paddingSmall))
                Column(modifier = Modifier.weight(1f)) {
                    Text(post.author, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(post.timeAgo, fontSize = 12.sp, color = TextDisabled)
                }
                IconButton(
//                    onClick = { viewModel.deleteFavoriteCommunityPost(post) },
                    onClick = { onRemoveClick(post) },
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "즐겨찾기에서 제거",
                        tint = Error
                    )
                }
            }
            Spacer(modifier = Modifier.height(paddingSmall))

            Text(
                text = post.content,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (post.image != null) {
                AsyncImage(
                    model = post.image,
                    contentDescription = "Post image",
                    modifier = Modifier
                        .padding(top = paddingSmall)
                        .fillMaxWidth()
                        .height(postImageHeight)
                        .clip(shapeImage),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder)
                )
            }
            Spacer(modifier = Modifier.height(paddingMedium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = CircleShape,
                    color = primaryContainer
                ) {
                    Text(
                        text = post.category,
                        color = OnPrimaryContainer,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(paddingMedium)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(paddingMicro))
                        Text(post.likes.toString(), fontSize = 14.sp, color = Primary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(paddingMicro))
                        Text(post.comments.toString(), fontSize = 14.sp, color = TextSecondary)
                    }
                }
            }
        }
    }
}
