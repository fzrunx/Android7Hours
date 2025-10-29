package com.sesac.home.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sesac.common.R as cR
import com.sesac.home.presentation.ui.CarouselItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.HomeCarousel(
    modifier: Modifier = Modifier,
    text: String?,
    recommendImages: List<CarouselItem>,
    textSpace: Dp = dimensionResource(cR.dimen.text_top_bottom_space),
    space: Dp = dimensionResource(cR.dimen.default_space),
    imageWidth: Dp = dimensionResource(cR.dimen.carousel_image_width),
    imageHeight: Dp = dimensionResource(cR.dimen.carousel_image_height),
) {
    Column {
        if(!text.isNullOrBlank()){
            Row {
                Text(
                    modifier = Modifier
                        .wrapContentSize(Alignment.CenterStart)
                        .weight(1f)
                        .padding(horizontal = textSpace, vertical = space),
                    text = text
                )

                Text(
                    modifier = Modifier
                        .wrapContentSize(Alignment.CenterEnd)
                        .weight(1f)
                        .padding(horizontal = textSpace, vertical = space),
                    color = Color(0xfff08130),
                    text = stringResource(cR.string.common_see_all)
                )
            }
        }

        HorizontalUncontainedCarousel(
            state = rememberCarouselState { recommendImages.count() },
            modifier = modifier
                .align(Alignment.Start)
                .wrapContentWidth()
                .padding(bottom = 0.dp),
            itemWidth = imageWidth,
            itemSpacing = space,
            contentPadding = PaddingValues(horizontal = space)
        ) { i ->
            val item = recommendImages[i]
            Image(
                modifier = Modifier
                    .height(imageHeight)
                    .maskClip(MaterialTheme.shapes.medium),
                painter = painterResource(id = item.imageResId),
                contentDescription = item.contentDescription,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}