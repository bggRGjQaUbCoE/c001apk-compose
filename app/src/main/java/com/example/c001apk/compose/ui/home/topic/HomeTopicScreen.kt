package com.example.c001apk.compose.ui.home.topic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.model.TopicBean
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.carousel.CarouselContentScreen
import com.example.c001apk.compose.ui.component.cards.LoadingCard
import com.example.c001apk.compose.ui.home.TabType
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/11
 */
@Composable
fun HomeTopicScreen(
    type: TabType,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
) {

    val viewModel =
        hiltViewModel<HomeTopicViewModel, HomeTopicViewModel.ViewModelFactory>(key = type.name) { factory ->
            factory.create(
                url = when (type) {
                    TabType.TOPIC -> "/v6/page/dataList?url=V11_VERTICAL_TOPIC&title=话题&page=1"
                    TabType.PRODUCT -> "/v6/product/categoryList"
                    else -> throw IllegalArgumentException("invalid type: $type")
                }
            )
        }
    val scope = rememberCoroutineScope()
    val currentIndex = when (type) {
        TabType.TOPIC -> 1
        TabType.PRODUCT -> 0
        else -> throw IllegalArgumentException("invalid type: $type")
    }
    val listState = rememberLazyListState()
    var pageState: PagerState
    var tabList: List<TopicBean>?

    Box(modifier = Modifier.fillMaxSize()) {
        when (viewModel.loadingState) {
            LoadingState.Loading, LoadingState.Empty, is LoadingState.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
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

            is LoadingState.Success -> {

                tabList = when (type) {
                    TabType.TOPIC -> (viewModel.loadingState as LoadingState.Success<List<HomeFeedResponse.Data>>)
                        .response.getOrNull(0)?.entities?.map {
                            TopicBean(it.url, it.title)
                        }

                    TabType.PRODUCT -> (viewModel.loadingState as LoadingState.Success<List<HomeFeedResponse.Data>>)
                        .response.map {
                            TopicBean(it.url.orEmpty(), it.title.orEmpty())
                        }

                    else -> throw IllegalArgumentException("invalid type: $type")
                }

                tabList?.let {
                    pageState = rememberPagerState(
                        initialPage = currentIndex,
                        pageCount = {
                            it.size
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                    ) {

                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.22f)
                        ) {
                            itemsIndexed(it, key = { _, item -> item.title }) { index, item ->
                                Row(
                                    modifier = Modifier
                                        .height(IntrinsicSize.Min)
                                        .fillMaxWidth()
                                        .clickable {
                                            scope.launch {
                                                pageState.scrollToPage(index)
                                            }
                                        }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(3.dp)
                                            .background(
                                                if (index == pageState.currentPage) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.surface
                                            )
                                    )
                                    Text(
                                        text = item.title,
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(
                                                if (index == pageState.currentPage)
                                                    MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                        3.dp
                                                    )
                                                else MaterialTheme.colorScheme.surface
                                            )
                                            .padding(8.dp)
                                            .align(Alignment.CenterVertically),
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        color = if (index == pageState.currentPage) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        VerticalPager(
                            state = pageState,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.78f)
                        ) { index ->
                            CarouselContentScreen(
                                url = it[index].url,
                                title = it[index].title,
                                bottomPadding = 0.dp,
                                refreshState = null,
                                resetRefreshState = {},
                                onViewUser = onViewUser,
                                onViewFeed = onViewFeed,
                                onOpenLink = onOpenLink,
                                onCopyText = onCopyText,
                                isHomeFeed = true,
                            )
                        }

                    }
                }
            }
        }
    }

}