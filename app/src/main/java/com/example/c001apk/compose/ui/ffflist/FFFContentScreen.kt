package com.example.c001apk.compose.ui.ffflist

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.ui.component.CommonScreen

/**
 * Created by bggRGjQaUbCoE on 2024/6/12
 */
@Composable
fun FFFContentScreen(
    uid: String,
    type: String,
    bottomPadding: Dp,
    refreshState: Boolean?,
    resetRefreshState: () -> Unit,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
) {

    val viewModel =
        hiltViewModel<FFFContentViewModel, FFFContentViewModel.ViewModelFactory>(key = uid + type) { factory ->
            factory.create(
                url = when (type) {
                    FFFListType.FEED.name -> "/v6/user/feedList?showAnonymous=0&isIncludeTop=1"
                    FFFListType.FOLLOW.name, FFFListType.USER_FOLLOW.name -> "/v6/user/followList"
                    FFFListType.APK.name -> "/v6/user/apkFollowList"
                    FFFListType.FAN.name -> "/v6/user/fansList"
                    FFFListType.RECENT.name -> "/v6/user/recentHistoryList"
                    FFFListType.LIKE.name -> "/v6/user/likeList"
                    FFFListType.REPLY.name -> "/v6/user/replyList"
                    FFFListType.REPLYME.name -> "/v6/user/replyToMeList"
                    else -> EMPTY_STRING
                }, uid = uid
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