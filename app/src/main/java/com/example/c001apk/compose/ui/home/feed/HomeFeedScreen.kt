package com.example.c001apk.compose.ui.home.feed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.FollowType
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.logic.providable.LocalUserPreferences
import com.example.c001apk.compose.ui.component.CommonScreen
import com.example.c001apk.compose.ui.home.TabType
import com.example.c001apk.compose.util.ReportType
import com.example.c001apk.compose.util.makeToast

/**
 * Created by bggRGjQaUbCoE on 2024/6/2
 */
@Composable
fun HomeFeedScreen(
    refreshState: Boolean,
    resetRefreshState: () -> Unit,
    type: TabType,
    paddingValues: PaddingValues,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, Boolean) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
    onReport: (String, ReportType) -> Unit,
) {

    val context = LocalContext.current
    val prefs = LocalUserPreferences.current

    val dataListUrl: String? = if (type == TabType.FOLLOW)
        when (prefs.followType) {
            FollowType.ALL -> "/page?url=V9_HOME_TAB_FOLLOW"
            FollowType.USER -> "/page?url=V9_HOME_TAB_FOLLOW&type=circle"
            FollowType.TOPIC -> "/page?url=V9_HOME_TAB_FOLLOW&type=topic"
            FollowType.PRODUCT -> "/page?url=V9_HOME_TAB_FOLLOW&type=product"
            FollowType.APP -> "/page?url=V9_HOME_TAB_FOLLOW&type=apk"
            FollowType.UNRECOGNIZED -> EMPTY_STRING
        }
    else null

    val dataListTitle: String? = if (type == TabType.FOLLOW)
        when (prefs.followType) {
            FollowType.ALL -> "全部关注"
            FollowType.USER -> "好友关注"
            FollowType.TOPIC -> "话题关注"
            FollowType.PRODUCT -> "数码关注"
            FollowType.APP -> "应用关注"
            FollowType.UNRECOGNIZED -> EMPTY_STRING
        }
    else null

    val viewModel =
        hiltViewModel<HomeFeedViewModel, HomeFeedViewModel.ViewModelFactory>(key = type.name) { factory ->
            factory.create(
                type = type,
                dataListUrl = when (type) {
                    TabType.FOLLOW -> dataListUrl ?: EMPTY_STRING
                    TabType.HOT -> "/page?url=V9_HOME_TAB_RANKING"
                    TabType.COOLPIC -> "/page?url=V11_FIND_COOLPIC"
                    else -> EMPTY_STRING
                },
                dataListTitle = when (type) {
                    TabType.FOLLOW -> dataListTitle ?: EMPTY_STRING
                    TabType.HOT -> "热榜"
                    TabType.COOLPIC -> "酷图"
                    else -> EMPTY_STRING
                },
                installTime = prefs.installTime
            )
        }

    LaunchedEffect(prefs.followType) {
        if (type == TabType.FOLLOW) {
            viewModel.dataListUrl = dataListUrl ?: EMPTY_STRING
            viewModel.dataListTitle = dataListTitle ?: EMPTY_STRING
        }
    }

    CommonScreen(
        viewModel = viewModel,
        refreshState = refreshState,
        resetRefreshState = resetRefreshState,
        paddingValues = paddingValues,
        onViewUser = onViewUser,
        onViewFeed = onViewFeed,
        onOpenLink = onOpenLink,
        onCopyText = onCopyText,
        onReport = onReport,
    )

    viewModel.toastText?.let{
        viewModel.resetToastText()
        context.makeToast(it)
    }

}