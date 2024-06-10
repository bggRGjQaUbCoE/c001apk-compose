package com.example.c001apk.compose.ui.topic

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.ui.component.CommonScreen

/**
 * Created by bggRGjQaUbCoE on 2024/6/9
 */
@Composable
fun TopicContentScreen(
    paddingValues: PaddingValues,
    refreshState: Boolean,
    resetRefreshState: () -> Unit,
    url: String,
    title: String,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
    onOpenLink: (String) -> Unit,
    onCopyText: (String?) -> Unit,
) {

    val viewModel =
        hiltViewModel<TopicContentViewModel, TopicContentViewModel.ViewModelFactory>(key = title) { factory ->
            factory.create(url, title)
        }

    CommonScreen(
        viewModel = viewModel,
        refreshState = refreshState,
        resetRefreshState = resetRefreshState,
        onViewUser = onViewUser,
        onViewFeed = onViewFeed,
        onOpenLink = onOpenLink,
        onCopyText = onCopyText,
    )

}