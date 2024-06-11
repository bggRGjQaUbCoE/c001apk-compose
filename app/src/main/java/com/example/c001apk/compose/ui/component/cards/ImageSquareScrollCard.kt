package com.example.c001apk.compose.ui.component.cards

import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.util.density
import com.example.c001apk.compose.util.screenHeight
import com.example.c001apk.compose.util.screenWidth
import jp.wasabeef.transformers.coil.ColorFilterTransformation
import kotlin.math.min

/**
 * Created by bggRGjQaUbCoE on 2024/6/6
 */
@Composable
fun ImageSquareScrollCard(
    modifier: Modifier = Modifier,
    entities: List<HomeFeedResponse.Entities>?,
    onOpenLink: (String, String?) -> Unit,
) {

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        entities?.forEach {
            item(it.title) {
                ImageSquareScrollCardItem(
                    pic = it.pic,
                    url = it.url,
                    title = it.title,
                    onOpenLink = onOpenLink,
                )
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
) {

    val context = LocalContext.current

    val itemWidth = (min(screenWidth, screenHeight) - 60 * density) / 5f / density

    Box(
        modifier = modifier
            .size(itemWidth.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onOpenLink(url, title)
            }
    ) {

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(pic)
                .transformations(ColorFilterTransformation(Color.parseColor("#8D000000")))
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = title,
            color = androidx.compose.ui.graphics.Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
        )

    }
}