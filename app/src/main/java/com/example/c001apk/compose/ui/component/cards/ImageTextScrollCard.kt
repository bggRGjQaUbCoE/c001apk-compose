package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.ui.component.CoilLoader
import com.example.c001apk.compose.ui.theme.cardBg

/**
 * Created by bggRGjQaUbCoE on 2024/6/6
 */
@Composable
fun ImageTextScrollCard(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onOpenLink: (String, String?) -> Unit,
) {

    BoxWithConstraints {

        val itemWidth = (maxWidth - 20.dp) / 3f * 2

        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            if (!data.title.isNullOrEmpty()) {
                TitleCard(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                    url = data.url.orEmpty(),
                    title = data.title,
                    onOpenLink = onOpenLink,
                )
            }

            data.entities?.let {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    it.forEach { item ->
                        item(key = item.id) {
                            ImageTextScrollCardItem(
                                url = item.url.orEmpty(),
                                pic = item.pic.orEmpty(),
                                title = item.title.orEmpty(),
                                onOpenLink = onOpenLink,
                                itemWidth = itemWidth,
                            )
                        }
                    }

                }
            }

        }
    }

}

@Composable
fun ImageTextScrollCardItem(
    modifier: Modifier = Modifier,
    url: String,
    pic: String,
    title: String,
    onOpenLink: (String, String?) -> Unit,
    itemWidth: Dp,
) {

    Column(
        modifier = modifier
            .width(itemWidth)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onOpenLink(url, title)
            }
    ) {
        CoilLoader(
            url = pic,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2.22f)
        )

        Text(
            text = title,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .background(cardBg())
                .padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }

}
