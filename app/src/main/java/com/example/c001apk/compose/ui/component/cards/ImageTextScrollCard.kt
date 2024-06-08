package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.util.density
import com.example.c001apk.compose.util.screenHeight
import com.example.c001apk.compose.util.screenWidth
import kotlin.math.min

/**
 * Created by bggRGjQaUbCoE on 2024/6/6
 */
@Composable
fun ImageTextScrollCard(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onTitleClick: (String) -> Unit,
    onOpenLink: (String) -> Unit,
) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (!data.title.isNullOrEmpty()) {
            TitleCard(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                url = data.url.orEmpty(),
                title = data.title,
                onTitleClick = onTitleClick,
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
                            url = item.url,
                            pic = item.pic,
                            title = item.title,
                            onOpenLink = onOpenLink
                        )
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
    onOpenLink: (String) -> Unit // "/feed/"
) {

    val context = LocalContext.current

    val itemWidth = (min(screenWidth, screenHeight) - 20 * density) / 3f * 2 / density

    Column(
        modifier = modifier
            .width(itemWidth.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onOpenLink(url)
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(pic)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
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
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }

}
