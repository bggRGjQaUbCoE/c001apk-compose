package com.example.c001apk.compose.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.component.cards.AppCard
import com.example.c001apk.compose.ui.component.cards.AppCardType
import com.example.c001apk.compose.ui.component.cards.CarouselCard
import com.example.c001apk.compose.ui.component.cards.FeedCard
import com.example.c001apk.compose.ui.component.cards.FeedReplyCard
import com.example.c001apk.compose.ui.component.cards.IconLinkGridCard
import com.example.c001apk.compose.ui.component.cards.IconMiniGridCard
import com.example.c001apk.compose.ui.component.cards.IconMiniScrollCard
import com.example.c001apk.compose.ui.component.cards.IconScrollCard
import com.example.c001apk.compose.ui.component.cards.ImageSquareScrollCard
import com.example.c001apk.compose.ui.component.cards.ImageTextScrollCard
import com.example.c001apk.compose.ui.component.cards.LoadingCard
import com.example.c001apk.compose.ui.component.cards.MessageCard
import com.example.c001apk.compose.ui.component.cards.NotificationCard
import com.example.c001apk.compose.ui.component.cards.TitleCard
import com.example.c001apk.compose.util.ReportType

/**
 * Created by bggRGjQaUbCoE on 2024/6/10
 */
fun LazyListScope.ItemCard(
    loadingState: LoadingState<List<HomeFeedResponse.Data>>,
    loadMore: () -> Unit,
    isEnd: Boolean,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, Boolean) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
    onShowTotalReply: (String, String) -> Unit,
    isHomeFeed: Boolean = false,
    onReport: (String, ReportType) -> Unit,
) {

    when (loadingState) {
        LoadingState.Loading, LoadingState.Empty, is LoadingState.Error -> {
            item(key = "loadingState") {
                Box(modifier = Modifier.fillParentMaxSize()) {
                    LoadingCard(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 10.dp),
                        state = loadingState,
                        onClick = if (loadingState is LoadingState.Loading) null
                        else loadMore
                    )
                }
            }
        }

        is LoadingState.Success -> {
            val dataList = loadingState.response
            itemsIndexed(dataList, key = { index, item -> item.entityId + index }) { index, item ->
                when (val type = item.entityType) {
                    "card" -> when (item.entityTemplate) {
                        "imageCarouselCard_1" -> CarouselCard(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            entities = item.entities,
                            onOpenLink = onOpenLink
                        )

                        "iconLinkGridCard" -> IconLinkGridCard(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            entities = item.entities,
                            onOpenLink = onOpenLink
                        )


                        "iconMiniScrollCard" -> IconMiniScrollCard(
                            data = item,
                            onOpenLink = onOpenLink
                        )

                        "iconMiniGridCard" -> IconMiniGridCard(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            data = item,
                            onOpenLink = onOpenLink
                        )

                        "imageSquareScrollCard" -> ImageSquareScrollCard(
                            entities = item.entities,
                            onOpenLink = onOpenLink
                        )

                        "titleCard" -> TitleCard(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            url = item.url.orEmpty(),
                            title = item.title.orEmpty(),
                            onOpenLink = onOpenLink
                        )

                        "iconScrollCard" -> IconScrollCard(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            data = item,
                            onOpenLink = onOpenLink
                        )

                        "imageTextScrollCard" -> ImageTextScrollCard(
                            data = item,
                            onOpenLink = onOpenLink
                        )

                    }

                    "feed" -> FeedCard(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        data = item,
                        onViewUser = onViewUser,
                        onViewFeed = onViewFeed,
                        isFeedContent = false,
                        onOpenLink = onOpenLink,
                        onCopyText = onCopyText,
                        onReport = onReport,
                    )

                    "feed_reply" -> {
                        FeedReplyCard(
                            data = item,
                            onViewUser = onViewUser,
                            onShowTotalReply = onShowTotalReply,
                            onOpenLink = onOpenLink,
                            onCopyText = onCopyText,
                            onReport = onReport,
                        )
                        if (item.fetchType == "feed_reply")
                            HorizontalDivider()
                    }

                    "apk", "product", "user", "topic", "contacts", "recentHistory" -> AppCard(
                        data = item,
                        onOpenLink = onOpenLink,
                        appCardType = when (type) {
                            "apk" -> AppCardType.APP
                            "product" -> AppCardType.PRODUCT
                            "user" -> AppCardType.USER
                            "topic" -> AppCardType.TOPIC
                            "contacts" -> AppCardType.CONTACTS
                            "recentHistory" -> AppCardType.RECENT
                            else -> throw IllegalArgumentException("invalid type: $type")
                        },
                        isHomeFeed = isHomeFeed,
                        onViewUser = onViewUser,
                    )

                    "notification" -> NotificationCard(
                        data = item,
                        onViewUser = onViewUser,
                        onOpenLink = onOpenLink,
                        onReport = onReport,
                    )

                    "message" -> MessageCard(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        data = item,
                        onOpenLink = onOpenLink,
                    )

                }

                if (index == dataList.lastIndex && !isEnd) {
                    loadMore()
                }
            }
        }
    }

}