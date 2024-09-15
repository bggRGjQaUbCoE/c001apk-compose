package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.ui.component.CoilLoader
import com.example.c001apk.compose.ui.theme.cardBg

/**
 * Created by bggRGjQaUbCoE on 2024/6/5
 */
@Composable
fun IconLinkGridCard(
    modifier: Modifier = Modifier,
    entities: List<HomeFeedResponse.Entities>?,
    onOpenLink: (String, String?) -> Unit
) {

    entities?.let {
        val pagerState = rememberPagerState { it.size / 5 }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(cardBg())
        ) {
            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                state = pagerState
            ) { index ->

                Row(modifier = Modifier.fillMaxWidth()) {
                    (0..4).forEach {
                        IconLinkGridCardItem(
                            Modifier.weight(1f),
                            entities.getOrNull(index * 5 + it)?.pic.orEmpty(),
                            entities.getOrNull(index * 5 + it)?.url.orEmpty(),
                            entities.getOrNull(index * 5 + it)?.title.orEmpty(),
                            onOpenLink
                        )
                    }
                }
            }

            if (pagerState.pageCount > 1) {
                CardIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    dimension = 5.dp,
                    defWidth = 1.5f,
                    selectedWidth = 2f,
                    defColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    pagerState = pagerState,
                )
            }

        }
    }

}

@Composable
fun IconLinkGridCardItem(
    modifier: Modifier = Modifier,
    pic: String,
    url: String,
    title: String,
    onOpenLink: (String, String?) -> Unit
) {

    Column(
        modifier = modifier
            .clickable {
                onOpenLink(url, title)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CoilLoader(
            url = pic,
            modifier = Modifier
                .padding(top = 4.dp)
                .size(30.dp)
        )

        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }

}