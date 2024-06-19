package com.example.c001apk.compose.ui.component.cards

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by bggRGjQaUbCoE on 2024/6/5
 */
@Composable
fun CardIndicator(
    modifier: Modifier = Modifier,
    dimension: Dp = 6.dp,
    defWidth: Float = 1.0f,
    selectedWidth: Float = 1.0f,
    defColor: Color = MaterialTheme.colorScheme.primaryContainer,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    pagerState: PagerState,
    isCarousel: Boolean = false
) {
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    val pageCount = with(pagerState.pageCount) {
        if (isCarousel) this - 2
        else this
    }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
        items(pageCount) { i ->
            val isSelected by remember {
                derivedStateOf {
                    with(currentPage) {
                        if (isCarousel)
                            when (currentPage) {
                                0 -> pageCount - 1
                                pageCount + 1 -> 0
                                else -> this - 1
                            }
                        else this
                    } == i
                }
            }
            val color by animateColorAsState(
                targetValue = if (isSelected) selectedColor else defColor,
                label = "indicatorColor"
            )
            val width by animateDpAsState(
                targetValue = if (isSelected) dimension.times(selectedWidth)
                else dimension.times(defWidth),
                label = "indicatorWidth"
            )

            Box(
                modifier = Modifier
                    .size(height = dimension, width = width)
                    .clip(CircleShape)
                    .background(color = color)
                    //.clickable { scope.launch { pagerState.animateScrollToPage(i) } }
            )
        }
    }
}