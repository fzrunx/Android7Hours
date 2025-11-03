package com.sesac.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.R

// 나중에 Entity(data)로 refactor
data class CommonArticlePreviewListItem(
    val title: String,
    var sumnail: Painter? = null,
    val content: String,
    val icon: ImageVector = Icons.Default.AccountCircle,
    val author: String,
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonArticlePreview(
    modifier: Modifier = Modifier,
    item: CommonArticlePreviewListItem,
    space: Dp = dimensionResource(R.dimen.default_space),
    imageHeight: Dp = dimensionResource(R.dimen.carousel_image_height),
    cardShape: Shape = RoundedCornerShape(15.dp),
    cardColors: CardColors = CardDefaults.cardColors(containerColor = Color(0xfff2e2d1)),
    cardElevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    titleTextFontSize: TextUnit = 13.sp,
    authorFontSize: TextUnit = 10.sp,
    contentFontSize: TextUnit = TextUnit.Unspecified,
    iconSize: Dp = 15.dp,
) {
    val currentItem = item.apply {
        sumnail = sumnail ?: painterResource(R.drawable.icons8_dog_50)
    }

    Card(
        modifier = modifier
//            .padding(horizontal = space)
            .fillMaxWidth()
            .height(imageHeight),
        shape = cardShape,
        colors = cardColors,
        elevation = cardElevation,
    ) {
        Row {
            Image(
                modifier = Modifier
                    .padding(horizontal = space, vertical = space)
                    .fillMaxHeight()
                    .weight(1f),
                painter = currentItem.sumnail!!,
                contentDescription = "community sample image"
            )

            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = space, top = space)
                        .weight(1f),
                    fontSize = titleTextFontSize,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    text = currentItem.title
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .wrapContentSize(Alignment.CenterStart)
                            .padding(start = space)
                            .size(iconSize),
                        imageVector = currentItem.icon,
                        contentDescription = "icon"
                    )

                    Text(
                        modifier = Modifier
                            .padding(horizontal = space / 3),
                        fontSize = authorFontSize,
                        maxLines = 1,
                        text = currentItem.author
                    )
                }

                Text(
                    modifier = Modifier
                        .weight(2f)
                        .padding(start = space),
                    fontSize = contentFontSize,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = currentItem.content
                )

            }

            Spacer(Modifier.padding(space))

        }
    }
}