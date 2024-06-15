package com.example.c001apk.compose.ui.ffflist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.ui.component.CommonScreen
import com.example.c001apk.compose.util.ReportType
import com.example.c001apk.compose.util.makeToast

/**
 * Created by bggRGjQaUbCoE on 2024/6/12
 */
@Composable
fun FFFContentScreen(
    uid: String?,
    id: String?,
    type: String,
    paddingValues: PaddingValues,
    refreshState: Boolean?,
    resetRefreshState: () -> Unit,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, Boolean) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
    onReport: (String, ReportType) -> Unit,
    onViewFFFList: (String?, String, String?, String?) -> Unit,
) {

    val viewModel =
        hiltViewModel<FFFContentViewModel, FFFContentViewModel.ViewModelFactory>(key = id + uid + type) { factory ->
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
                    FFFListType.COLLECTION.name -> "/v6/collection/list"
                    FFFListType.COLLECTION_ITEM.name -> "/v6/collection/itemList"
                    else -> EMPTY_STRING
                },
                uid = uid, id = id, showDefault = if (type.contains("COLLECTION")) 0 else null
            )
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
        onViewFFFList = onViewFFFList,
    )

    val context = LocalContext.current
    viewModel.toastText?.let{
        viewModel.resetToastText()
        context.makeToast(it)
    }

}