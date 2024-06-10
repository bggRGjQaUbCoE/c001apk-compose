package com.example.c001apk.compose.ui.app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.ui.component.CommonScreen

/**
 * Created by bggRGjQaUbCoE on 2024/6/10
 */
@Composable
fun AppContentScreen(
    refreshState: Boolean,
    resetRefreshState: () -> Unit,
    id: String,
    appCommentSort: String,
    appCommentTitle: String,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
    onOpenLink: (String) -> Unit,
    onCopyText: (String?) -> Unit,
) {

    val viewModel =
        hiltViewModel<AppContentViewModel, AppContentViewModel.ViewModelFactory>(key = appCommentSort) { factory ->
            factory.create(id, appCommentSort, appCommentTitle)
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