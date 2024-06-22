package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.ui.component.CoilLoader
import com.example.c001apk.compose.util.density
import com.example.c001apk.compose.util.screenHeight
import com.example.c001apk.compose.util.screenWidth
import kotlin.math.min

/**
 * Created by bggRGjQaUbCoE on 2024/6/6
 */
@Composable
fun IconScrollCard(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onOpenLink: (String, String?) -> Unit,
) {

    val itemWidth by lazy {
        (min(screenWidth, screenHeight) - 30 * density) / 9f * 2 / density
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
    ) {

        if (!data.title.isNullOrEmpty()) {
            TitleCard(
                modifier = Modifier.padding(top = 10.dp),
                url = data.url.orEmpty(),
                title = data.title,
                onOpenLink = onOpenLink,
            )
        }

        data.entities?.let {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                it.forEach { item ->
                    item(key = item.uid) {
                        IconScrollCardItem(
                            url = item.url.orEmpty(),
                            avatar = item.userAvatar.orEmpty(),
                            username = item.username.orEmpty(),
                            onOpenLink = onOpenLink,
                            itemWidth = itemWidth,
                        )
                    }
                }

            }
        }

    }

}

@Composable
fun IconScrollCardItem(
    modifier: Modifier = Modifier,
    url: String,
    avatar: String,
    username: String,
    onOpenLink: (String, String?) -> Unit,
    itemWidth: Float,
) {

    Column(
        modifier = modifier
            .width(itemWidth.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onOpenLink(url, null)
            }
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CoilLoader(
            url = avatar,
            modifier = Modifier
                .size((itemWidth / 3f * 2).dp)
                .clip(CircleShape)
        )

        Text(
            text = username,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 5.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }

}