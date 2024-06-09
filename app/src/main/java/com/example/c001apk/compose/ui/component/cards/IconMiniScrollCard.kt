package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.c001apk.compose.logic.model.HomeFeedResponse

/**
 * Created by bggRGjQaUbCoE on 2024/6/6
 */
@Composable
fun IconMiniScrollCard(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onOpenLink: (String) -> Unit
) {

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (!data.title.isNullOrEmpty()) {
            item {
                Text(
                    text = data.title,
                    fontSize = 15.sp
                )
            }
        }

        data.entities?.forEach {
            item(key = it.id) {
                IconMiniScrollCardItem(
                    isFeedContent = true,
                    logoUrl = it.logo.orEmpty(),
                    linkUrl = it.url,
                    titleText = it.title,
                    onOpenLink = onOpenLink
                )
            }
        }

    }

}

@Composable
fun IconMiniScrollCardItem(
    modifier: Modifier = Modifier,
    isFeedContent: Boolean,
    logoUrl: String,
    linkUrl: String,
    titleText: String,
    onOpenLink: (String) -> Unit,
    isGridCard: Boolean = false,
) {

    val context = LocalContext.current

    ConstraintLayout(
        modifier = modifier
            .clip(if (isGridCard) RectangleShape else RoundedCornerShape(8.dp))
            .background(
                if (isFeedContent) MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                else MaterialTheme.colorScheme.surface
            )
            .clickable {
                onOpenLink(linkUrl)
            }
            .padding(start = if (isGridCard) 10.dp else 5.dp, end = 5.dp)
            .padding(vertical = 5.dp)
    ) {
        val (logo, title) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(logoUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .aspectRatio(1f)
                .constrainAs(logo) {
                    start.linkTo(parent.start)
                    top.linkTo(title.top)
                    bottom.linkTo(title.bottom)
                    height = Dimension.fillToConstraints
                }
        )
        Text(
            text = titleText,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            modifier = Modifier
                .padding(start = 5.dp)
                .constrainAs(title) {
                    start.linkTo(logo.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
        )
    }

}