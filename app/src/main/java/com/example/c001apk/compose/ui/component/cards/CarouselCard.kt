package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import kotlinx.coroutines.delay

/**
 * Created by bggRGjQaUbCoE on 2024/6/3
 */
@Composable
fun CarouselCard(
    modifier: Modifier = Modifier,
    entities: List<HomeFeedResponse.Entities>?,
    onClick: (String) -> Unit
) {

    entities?.let {
        val context = LocalContext.current
        Box {
            val dataList = it.toMutableList()
            if (it.size > 1) {
                val tmp = it.first()
                dataList.add(0, it.last())
                dataList.add(dataList.size, tmp)
            }

            val pagerState =
                rememberPagerState(initialPage = if (it.size > 1) 1 else 0) { dataList.size }

            if (it.size > 1) {
                if (pagerState.currentPage == 0) {
                    LaunchedEffect(Unit) {
                        delay(500)
                        pagerState.scrollToPage(pagerState.pageCount - 2)
                    }
                } else if (pagerState.currentPage == pagerState.pageCount - 1) {
                    LaunchedEffect(Unit) {
                        delay(500)
                        pagerState.scrollToPage(1)
                    }
                }
            }

            HorizontalPager(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                state = pagerState
            ) { index ->
                val item = dataList[index]
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(item.pic)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f)
                        .clickable {
                            onClick(item.url)
                        }
                )
            }

            if (pagerState.pageCount > 1) {
                CardIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 5.dp, end = 20.dp),
                    pagerState = pagerState,
                    isCarousel = true
                )
            }

        }
    }
}