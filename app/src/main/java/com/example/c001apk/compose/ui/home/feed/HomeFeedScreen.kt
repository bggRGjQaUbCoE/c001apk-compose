package com.example.c001apk.compose.ui.home.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.c001apk.compose.ui.component.FooterCard
import com.example.c001apk.compose.ui.component.ItemCard
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
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
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