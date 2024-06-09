package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.c001apk.compose.logic.model.HomeFeedResponse

/**
 * Created by bggRGjQaUbCoE on 2024/6/5
 */
@Composable
fun IconLinkGridCard(
    modifier: Modifier = Modifier,
    entities: List<HomeFeedResponse.Entities>?,
    onOpenLink: (String) -> Unit
) {

    entities?.let {
        val pagerState = rememberPagerState { it.size / 5 }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
        ) {
            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                state = pagerState
            ) { index ->

                Row(modifier = Modifier.fillMaxWidth()) {
                    (0..4).forEach {
                        IconLinkGridCardItem(
                            Modifier.weight(1f),
                            entities.getOrNull(index * 5 + it)?.pic.orEmpty(),
                            entities.getOrNull(index * 5 + it)?.url.orEmpty(),
                            entities.getOrNull(index * 5 + it)?.title.orEmpty(),
                            onOpenLink
                        )
                    }
                }
            }

            if (pagerState.pageCount > 1) {
                CardIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    dimension = 5.dp,
                    defWidth = 1.5f,
                    selectedWidth = 2f,
                    defColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    pagerState = pagerState,
                )
            }

        }
    }

}

@Composable
fun IconLinkGridCardItem(
    modifier: Modifier = Modifier,
    pic: String,
    url: String,
    title: String,
    onOpenLink: (String) -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = modifier
            .clickable {
                onOpenLink(url)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(pic)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }

}