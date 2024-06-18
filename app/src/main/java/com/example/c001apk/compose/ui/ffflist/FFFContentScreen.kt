package com.example.c001apk.compose.ui.ffflist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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

    var showRecentDialog by remember { mutableStateOf(false) }

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
        onHandleRecent = { actionId, targetId, targetType, isTop ->
            viewModel.actionId = actionId
            viewModel.targetId = targetId
            viewModel.targetType = targetType
            viewModel.isTop = isTop
            showRecentDialog = true
        },
    )

    val context = LocalContext.current
    viewModel.toastText?.let {
        viewModel.resetToastText()
        context.makeToast(it)
    }

    when {
        showRecentDialog -> {
            Dialog(onDismissRequest = { showRecentDialog = false }) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.elevatedCardColors()
                        .copy(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = if (viewModel.isTop == 1) "移除置顶" else "置顶",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showRecentDialog = false
                                    viewModel.onHandleRecentHistory(FFFContentViewModel.ActionType.TOP)
                                }
                                .padding(horizontal = 24.dp, vertical = 14.dp),
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "删除",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showRecentDialog = false
                                    viewModel.onHandleRecentHistory(FFFContentViewModel.ActionType.DELETE)
                                }
                                .padding(horizontal = 24.dp, vertical = 14.dp),
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "清空全部",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showRecentDialog = false
                                    viewModel.onHandleRecentHistory(FFFContentViewModel.ActionType.DELETE_ALL)
                                }
                                .padding(horizontal = 24.dp, vertical = 14.dp),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }

}