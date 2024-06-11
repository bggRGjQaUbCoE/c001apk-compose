package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.c001apk.compose.logic.model.HomeFeedResponse

/**
 * Created by bggRGjQaUbCoE on 2024/6/7
 */
@Composable
fun IconMiniGridCard(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onOpenLink: (String, String?) -> Unit
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            .padding(vertical = 5.dp)
    ) {

        if (!data.title.isNullOrEmpty()) {
            TitleCard(
                modifier = Modifier.padding(bottom = 5.dp),
                url = data.url.orEmpty(),
                title = data.title,
                onOpenLink = onOpenLink,
            )
        }

        data.entities?.let {
            val columnCount = it.size / 2
            (0 until columnCount).forEach { column ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    (0..1).forEach { row ->
                        IconMiniScrollCardItem(
                            modifier = Modifier.weight(1f),
                            isFeedContent = true,
                            isGridCard = true,
                            logoUrl = it.getOrNull(column * 2 + row)?.logo.orEmpty(),
                            linkUrl = it.getOrNull(column * 2 + row)?.url.orEmpty(),
                            titleText = it.getOrNull(column * 2 + row)?.title.orEmpty(),
                            onOpenLink = onOpenLink
                        )
                    }
                }
            }
        }

    }

}