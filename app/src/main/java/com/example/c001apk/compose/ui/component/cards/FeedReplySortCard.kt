package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by bggRGjQaUbCoE on 2024/6/12
 */
@Composable
fun FeedReplySortCard(
    modifier: Modifier = Modifier,
    replyCount: String,
    selected: Int = 0,
    updateSortReply: (Int) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        HorizontalDivider()

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "共 $replyCount 回复",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
                    .padding(vertical = 2.dp),
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            VerticalDivider()

            FeedReplySortCardItem(
                title = "默认",
                isSelected = selected == 0,
                updateSortReply = {
                    updateSortReply(0)
                }
            )

            FeedReplySortCardItem(
                title = "最新",
                isSelected = selected == 1,
                updateSortReply = {
                    updateSortReply(1)
                }
            )

            FeedReplySortCardItem(
                title = "热门",
                isSelected = selected == 2,
                updateSortReply = {
                    updateSortReply(2)
                }
            )

            FeedReplySortCardItem(
                modifier = Modifier.padding(end = 16.dp),
                title = "楼主",
                isSelected = selected == 3,
                updateSortReply = {
                    updateSortReply(3)
                }
            )

        }

        HorizontalDivider()
    }

}

@Composable
fun FeedReplySortCardItem(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    updateSortReply: () -> Unit,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .clickable {
                updateSortReply()
            }
            .background(
                if (isSelected)
                    MaterialTheme.colorScheme.secondaryContainer
                else
                    MaterialTheme.colorScheme.surface
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
    )

    VerticalDivider(modifier = modifier)
}