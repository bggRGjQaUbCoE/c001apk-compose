package com.example.c001apk.compose.ui.notification

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.CommonScreen
import com.example.c001apk.compose.util.ReportType

/**
 * Created by bggRGjQaUbCoE on 2024/6/13
 */

enum class NoticeType {
    AT, COMMENT, LIKE, FOLLOW, MESSAGE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeScreen(
    onBackClick: () -> Unit,
    type: String,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, Boolean) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
    onReport: (String, ReportType) -> Unit,
) {

    val viewModel =
        hiltViewModel<NoticeViewModel, NoticeViewModel.ViewModelFactory>(key = type) { factory ->
            factory.create(
                url = when (type) {
                    NoticeType.AT.name -> "/v6/notification/atMeList"
                    NoticeType.COMMENT.name -> "/v6/notification/atCommentMeList"
                    NoticeType.LIKE.name -> "/v6/notification/feedLikeList"
                    NoticeType.FOLLOW.name -> "/v6/notification/contactsFollowList"
                    NoticeType.MESSAGE.name -> "/v6/message/list"
                    else -> throw IllegalArgumentException("invalid type: $type")
                }
            )
        }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton { onBackClick() }
                },
                title = {
                    Text(
                        text = when (type) {
                            NoticeType.AT.name -> "@我的动态"
                            NoticeType.COMMENT.name -> "@我的评论"
                            NoticeType.LIKE.name -> "我收到的赞"
                            NoticeType.FOLLOW.name -> "好友关注"
                            NoticeType.MESSAGE.name -> "私信"
                            else -> throw IllegalArgumentException("invalid type: $type")
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) { paddingValues ->

        CommonScreen(
            viewModel = viewModel,
            refreshState = null,
            resetRefreshState = {},
            paddingValues = paddingValues,
            needTopPadding = true,
            onViewUser = onViewUser,
            onViewFeed = onViewFeed,
            onOpenLink = onOpenLink,
            onCopyText = onCopyText,
            onReport = onReport,
            onViewFFFList = { _, _, _, _ -> },
        )
    }

}