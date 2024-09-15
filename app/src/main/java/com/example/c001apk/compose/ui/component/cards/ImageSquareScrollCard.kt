package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.ui.component.CoilLoader

/**
 * Created by bggRGjQaUbCoE on 2024/6/6
 */
@Composable
fun ImageSquareScrollCard(
    modifier: Modifier = Modifier,
    entities: List<HomeFeedResponse.Entities>?,
    onOpenLink: (String, String?) -> Unit,
) {

    BoxWithConstraints {

        val itemWidth = (maxWidth - 60.dp) / 5f

        LazyRow(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            entities?.forEach {
                item(it.title) {
                    ImageSquareScrollCardItem(
                        pic = it.pic.orEmpty(),
                        url = it.url.orEmpty(),
                        title = it.title.orEmpty(),
                        onOpenLink = onOpenLink,
                        itemWidth = itemWidth,
                    )
                }
            }

        }
    }

}

@Composable
fun ImageSquareScrollCardItem(
    modifier: Modifier = Modifier,
    pic: String,
    url: String,
    title: String,
    onOpenLink: (String, String?) -> Unit,
    itemWidth: Dp,
) {

    Box(
        modifier = modifier
            .size(itemWidth)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onOpenLink(url, title)
            }
    ) {

        CoilLoader(
            url = pic,
            colorFilter = 0x8D000000,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = title,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

    }
}