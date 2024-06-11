package com.example.c001apk.compose.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.core.view.isVisible
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.ui.component.CommonScreen

/**
 * Created by bggRGjQaUbCoE on 2024/6/9
 */
@Composable
fun SearchContentScreen(
    searchType: SearchType,
    keyword: String,
    pageType: String?,
    pageParam: String?,
    refreshState: Boolean,
    resetRefreshState: () -> Unit,
    bottomPadding: Dp,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
    updateInitPage: () -> Unit,
) {

    val view = LocalView.current
    if (view.isVisible) {
        updateInitPage()
    }

    val viewModel =
        hiltViewModel<SearchContentViewModel, SearchContentViewModel.ViewModelFactory>(key = keyword + searchType.name) { factory ->
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

    CommonScreen(
        viewModel = viewModel,
        refreshState = refreshState,
        resetRefreshState = resetRefreshState,
        bottomPadding = bottomPadding,
        onViewUser = onViewUser,
        onViewFeed = onViewFeed,
        onOpenLink = onOpenLink,
        onCopyText = onCopyText,
    )

}