package com.example.c001apk.compose.ui.home.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.state.FooterState
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.component.cards.CarouselCard
import com.example.c001apk.compose.ui.component.cards.FeedCard
import com.example.c001apk.compose.ui.component.cards.IconLinkGridCard
import com.example.c001apk.compose.ui.component.cards.IconMiniGridCard
import com.example.c001apk.compose.ui.component.cards.IconMiniScrollCard
import com.example.c001apk.compose.ui.component.cards.IconScrollCard
import com.example.c001apk.compose.ui.component.cards.ImageSquareScrollCard
import com.example.c001apk.compose.ui.component.cards.ImageTextScrollCard
import com.example.c001apk.compose.ui.component.cards.LoadingCard
import com.example.c001apk.compose.ui.component.cards.TitleCard
import com.example.c001apk.compose.ui.home.TabType
import com.example.c001apk.compose.util.TokenDeviceUtils.getLastingInstallTime
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/2
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFeedScreen(
    refreshState: Boolean,
    resetRefreshState: () -> Unit,
    type: TabType = TabType.FEED,
    onViewUser: (String) -> Unit, //uid
    onViewFeed: (String, String?) -> Unit, //id, rid
    onOpenLink: (String) -> Unit,
    onCopyText: (String?) -> Unit,
) {

    val context = LocalContext.current

    val viewModel =
        hiltViewModel<HomeFeedViewModel, HomeFeedViewModel.ViewModelFactory>(key = type.name) { factory ->
            factory.create(type, getLastingInstallTime(context))
        }

    val view = LocalView.current

    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()
    val lazyListState = rememberLazyListState()

    if (refreshState) {
        resetRefreshState()
        if (view.isVisible) {
            viewModel.refresh()
            LaunchedEffect(Unit) {
                scope.launch {
                    lazyListState.scrollToItem(0)
                }
            }
        }
    }

    PullToRefreshBox(
        state = state,
        isRefreshing = viewModel.isRefreshing,
        onRefresh = viewModel::refresh,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = viewModel.isRefreshing,
                state = state,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            when (viewModel.loadingState) {
                LoadingState.Loading, LoadingState.Empty, is LoadingState.Error -> {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize()) {
                            LoadingCard(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(horizontal = 10.dp),
                                state = viewModel.loadingState,
                                onClick = if (viewModel.loadingState is LoadingState.Loading) null
                                else viewModel::loadMore
                            )
                        }
                    }
                }

                is LoadingState.Success -> {
                    val dataList =
                        (viewModel.loadingState as LoadingState.Success<List<HomeFeedResponse.Data>>).response
                    itemsIndexed(dataList) { index, item ->
                        when (item.entityType) {
                            "card" -> when (item.entityTemplate) {
                                "imageCarouselCard_1" -> CarouselCard(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    entities = item.entities,
                                    onClick = {

                                    }
                                )

                                "iconLinkGridCard" -> IconLinkGridCard(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    entities = item.entities,
                                    onClick = {

                                    }
                                )


                                "iconMiniScrollCard" -> IconMiniScrollCard(
                                    data = item,
                                    onViewTopic = {

                                    }
                                )

                                "iconMiniGridCard" -> IconMiniGridCard(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    entities = item.entities,
                                    onViewTopic = {

                                    }
                                )


                                "imageSquareScrollCard" -> ImageSquareScrollCard(
                                    entities = item.entities,
                                    onClick = {

                                    }
                                )

                                "titleCard" -> TitleCard(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    url = item.url.orEmpty(),
                                    title = item.title.orEmpty(),
                                    onTitleClick = {

                                    }
                                )

                                "iconScrollCard" -> IconScrollCard(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    data = item,
                                    onTitleClick = {

                                    },
                                    onOpenLink = onOpenLink
                                )

                                "imageTextScrollCard" -> ImageTextScrollCard(
                                    data = item,
                                    onTitleClick = {

                                    },
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
                            )

                        }

                        if (index == dataList.lastIndex && !viewModel.isEnd) {
                            viewModel.loadMore()
                        }
                    }
                }
            }

            item {
                LoadingCard(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    state = viewModel.footerState,
                    onClick = if (viewModel.footerState is FooterState.Error) viewModel::loadMore
                    else null
                )
            }

        }

    }
}