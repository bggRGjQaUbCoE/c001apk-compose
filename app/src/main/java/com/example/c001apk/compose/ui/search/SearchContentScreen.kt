package com.example.c001apk.compose.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.ui.component.FooterCard
import com.example.c001apk.compose.ui.component.ItemCard
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/9
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContentScreen(
    paddingValues: PaddingValues,
    searchType: SearchType,
    keyword: String,
    pageType: String?,
    pageParam: String?,
    refreshState: Boolean,
    resetRefreshState: () -> Unit,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
    onOpenLink: (String) -> Unit,
    onCopyText: (String?) -> Unit,
) {

    val viewModel =
        hiltViewModel<SearchContentViewModel, SearchContentViewModel.ViewModelFactory>(key = searchType.name) { factory ->
            factory.create(
                type = when (searchType) {
                    SearchType.FEED -> "feed"
                    SearchType.APP -> "apk"
                    SearchType.GAME -> "game"
                    SearchType.PRODUCT -> "product"
                    SearchType.USER -> "user"
                    SearchType.TOPIC -> "feedTopic"
                },
                keyword = keyword, pageType = pageType, pageParam = pageParam
            )
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
            PullToRefreshDefaults.Indicator(
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
            contentPadding = PaddingValues(
                top = 10.dp,
                bottom = paddingValues.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            state = lazyListState
        ) {

            ItemCard(
                loadingState = viewModel.loadingState,
                loadMore = viewModel::loadMore,
                isEnd = viewModel.isEnd,
                onViewUser = onViewUser,
                onViewFeed = onViewFeed,
                onOpenLink = onOpenLink,
                onCopyText = onCopyText,
                onShowTotalReply = {},
            )

            FooterCard(
                modifier = Modifier.padding(horizontal = 10.dp),
                footerState = viewModel.footerState,
                loadMore = viewModel::loadMore,
            )
        }
    }

}