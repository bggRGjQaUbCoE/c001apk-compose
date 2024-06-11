package com.example.c001apk.compose.ui.home.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.ui.component.CommonScreen
import com.example.c001apk.compose.ui.home.TabType
import com.example.c001apk.compose.util.TokenDeviceUtils.getLastingInstallTime

/**
 * Created by bggRGjQaUbCoE on 2024/6/2
 */
@Composable
fun HomeFeedScreen(
    refreshState: Boolean,
    resetRefreshState: () -> Unit,
    type: TabType,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
) {

    val context = LocalContext.current

    val viewModel =
        hiltViewModel<HomeFeedViewModel, HomeFeedViewModel.ViewModelFactory>(key = type.name) { factory ->
            factory.create(type, getLastingInstallTime(context))
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