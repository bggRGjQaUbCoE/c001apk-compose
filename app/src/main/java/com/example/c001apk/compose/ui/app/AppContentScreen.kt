package com.example.c001apk.compose.ui.app

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.ui.component.CommonScreen
import com.example.c001apk.compose.util.ReportType
import com.example.c001apk.compose.util.density

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
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
    onReport: (String, ReportType) -> Unit,
) {

    val viewModel =
        hiltViewModel<AppContentViewModel, AppContentViewModel.ViewModelFactory>(key = appCommentSort) { factory ->
            factory.create(id, appCommentSort, appCommentTitle)
        }

    val context = LocalContext.current

    CommonScreen(
        viewModel = viewModel,
        refreshState = refreshState,
        resetRefreshState = resetRefreshState,
        bottomPadding = (WindowInsets.navigationBars.getBottom(Density(context)) / density).dp,
        onViewUser = onViewUser,
        onViewFeed = onViewFeed,
        onOpenLink = onOpenLink,
        onCopyText = onCopyText,
        onReport = onReport,
    )

}