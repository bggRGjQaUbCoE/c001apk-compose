package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.c001apk.compose.ui.theme.cardBg

/**
 * Created by bggRGjQaUbCoE on 2024/6/16
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchHistoryCard(
    modifier: Modifier = Modifier,
    data: String,
    onSearch: () -> Unit,
    onDelete: () -> Unit,
) {

    Text(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(cardBg())
            .combinedClickable(
                onClick = onSearch,
                onLongClick = onDelete
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        text = data,
        style = MaterialTheme.typography.titleSmall,
    )

}