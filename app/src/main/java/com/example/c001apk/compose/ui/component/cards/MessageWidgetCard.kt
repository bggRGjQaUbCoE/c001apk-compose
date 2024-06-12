package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * Created by bggRGjQaUbCoE on 2024/6/10
 */
@Composable
fun MessageWidgetCard(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))

    ) {
        FFFCardRow(
            dataList = listOf(
                FFFCardItem(title = "本地收藏", value = null, imageVector = Icons.Outlined.Archive),
                FFFCardItem(title = "浏览历史", value = null, imageVector = Icons.Outlined.History),
                FFFCardItem(title = "我的常去", value = null, imageVector = Icons.Outlined.MyLocation)
            )
        )
        FFFCardRow(
            dataList = listOf(
                FFFCardItem(title = "我的收藏", value = null, imageVector = Icons.Outlined.StarOutline),
                FFFCardItem(title = "我的赞", value = null, imageVector = Icons.Outlined.FavoriteBorder),
                FFFCardItem(title = "我的回复", value = null, imageVector = Icons.Outlined.ChatBubbleOutline)
            )
        )
    }

}