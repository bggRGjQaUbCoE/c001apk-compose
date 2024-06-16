package com.example.c001apk.compose.ui.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.base.LikeType
import com.example.c001apk.compose.ui.component.ArticleItem
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.FooterCard
import com.example.c001apk.compose.ui.component.ItemCard
import com.example.c001apk.compose.ui.component.cards.FeedCard
import com.example.c001apk.compose.ui.component.cards.FeedHeader
import com.example.c001apk.compose.ui.component.cards.FeedReplyCard
import com.example.c001apk.compose.ui.component.cards.FeedReplySortCard
import com.example.c001apk.compose.ui.component.cards.LoadingCard
import com.example.c001apk.compose.util.CookieUtil.isLogin
import com.example.c001apk.compose.util.ReportType
import com.example.c001apk.compose.util.ShareType
import com.example.c001apk.compose.util.Utils.richToString
import com.example.c001apk.compose.util.copyText
import com.example.c001apk.compose.util.getAllLinkAndText
import com.example.c001apk.compose.util.getShareText
import com.example.c001apk.compose.util.makeToast
import com.example.c001apk.compose.util.noRippleClickable
import com.example.c001apk.compose.util.shareText
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/4
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onBackClick: () -> Unit,
    id: String,
    isViewReply: Boolean,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, Boolean) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
    onReport: (String, ReportType) -> Unit,
) {

    val viewModel =
        hiltViewModel<FeedViewModel, FeedViewModel.ViewModelFactory>(key = id) { factory ->
            factory.create(id, isViewReply)
        }

    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    val state = rememberPullToRefreshState()
    val lazyListState = rememberLazyListState()
    var dropdownMenuExpanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(true)
    var openBottomSheet by remember { mutableStateOf(false) }
    var selected by rememberSaveable { mutableIntStateOf(0) }
    val shouldShowSortCard by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > viewModel.itemSize - 1 }
    }

    val dataList = remember(key1 = viewModel.loadingState) {
        (viewModel.loadingState as? LoadingState.Success)?.response ?: emptyList()
    }

    val articleList = remember(key1 = viewModel.feedState) { viewModel.articleList }

    val replyList = remember(key1 = viewModel.replyLoadingState) {
        (viewModel.replyLoadingState as? LoadingState.Success)?.response ?: emptyList()
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton { onBackClick() }
                },
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable {
                                scope.launch {
                                    lazyListState.scrollToItem(if (shouldShowSortCard) 0 else viewModel.itemSize)
                                }
                            },
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        AnimatedVisibility(
                            visible = !shouldShowSortCard,
                            enter = fadeIn(animationSpec = spring(stiffness = StiffnessLow)),
                            exit = fadeOut(animationSpec = spring(stiffness = StiffnessLow))
                        ) {
                            Text(text = viewModel.feedTypeName)
                        }
                    }
                    AnimatedVisibility(
                        visible = shouldShowSortCard,
                        enter = fadeIn(animationSpec = spring(stiffness = StiffnessLow)),
                        exit = fadeOut(animationSpec = spring(stiffness = StiffnessLow))
                    ) {
                        FeedHeader(
                            data = (viewModel.feedState as LoadingState.Success).response,
                            onViewUser = onViewUser,
                            isFeedContent = true,
                            onReport = onReport,
                            isFeedTop = true,
                            onDelete = { _, _ -> },
                            onBlockUser = {},
                        )
                    }
                },
                actions = {
                    if (viewModel.feedState is LoadingState.Success) {
                        Box(Modifier.wrapContentSize(Alignment.TopEnd)) {
                            IconButton(onClick = { dropdownMenuExpanded = true }) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                            }
                            DropdownMenu(
                                expanded = dropdownMenuExpanded,
                                onDismissRequest = { dropdownMenuExpanded = false }
                            ) {
                                listOf("Copy", "Share", "Fav", "Block")
                                    .forEachIndexed { index, menu ->
                                        DropdownMenuItem(
                                            text = { Text(menu) },
                                            onClick = {
                                                dropdownMenuExpanded = false
                                                when (index) {
                                                    0 -> context.copyText(
                                                        getShareText(ShareType.FEED, id)
                                                    )

                                                    1 -> context.shareText(
                                                        getShareText(ShareType.FEED, id)
                                                    )
                                                }
                                            }
                                        )
                                    }
                                if (isLogin) {
                                    DropdownMenuItem(
                                        text = { Text("Report") },
                                        onClick = {
                                            dropdownMenuExpanded = false
                                            onReport(id, ReportType.FEED)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            )
        },
        /*floatingActionButton = {
            AnimatedVisibility(
                visible = lazyListState.isScrollingUp(),
                enter = slideInVertically { it * 2 },
                exit = slideOutVertically { it * 2 }
            ) {
                FloatingActionButton(
                    onClick = { */
        /*TODO*/
        /* }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Reply,
                                contentDescription = null
                            )
                        }
                    }
                }*/
    ) { paddingValues ->

        PullToRefreshBox(
            modifier = Modifier.padding(
                start = paddingValues.calculateLeftPadding(layoutDirection),
                end = paddingValues.calculateRightPadding(layoutDirection),
                top = paddingValues.calculateTopPadding()
            ),
            state = state,
            isRefreshing = viewModel.isRefreshing,
            onRefresh = {
                viewModel.isPull = true
                viewModel.refresh()
            },
            indicator = {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = viewModel.isRefreshing,
                    state = state,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
                state = lazyListState
            ) {
                when (viewModel.feedState) {
                    LoadingState.Loading, LoadingState.Empty, is LoadingState.Error -> {
                        item(key = "feedState") {
                            Box(modifier = Modifier.fillParentMaxSize()) {
                                LoadingCard(
                                    modifier = Modifier.align(Alignment.Center),
                                    state = viewModel.feedState,
                                    onClick = if (viewModel.feedState is LoadingState.Loading) null
                                    else viewModel::refresh,
                                    isFeed = true,
                                )
                            }
                        }
                    }

                    is LoadingState.Success -> {
                        val response = (viewModel.feedState as LoadingState.Success).response
                        if (!articleList.isNullOrEmpty()) {
                            ArticleItem(
                                response = response,
                                articleList = articleList,
                                onOpenLink = onOpenLink,
                                onCopyText = onCopyText,
                                onLike = {
                                    if (isLogin) {
                                        viewModel.onLike(
                                            response.id.orEmpty(),
                                            response.userAction?.like ?: 0,
                                            LikeType.FEED
                                        )
                                    }
                                },
                            )
                        } else {
                            item(key = "feed") {
                                FeedCard(
                                    isFeedContent = true,
                                    data = response,
                                    onViewUser = onViewUser,
                                    onViewFeed = onViewFeed,
                                    onOpenLink = onOpenLink,
                                    onCopyText = onCopyText,
                                    onReport = onReport,
                                    onLike = { id, like, likeType ->
                                        viewModel.onLike(id, like, likeType)
                                    },
                                    onDelete = { id, deleteType ->
                                        viewModel.onDelete(id, deleteType)
                                    },
                                    onBlockUser = {},
                                )
                            }
                        }

                        item(key = "sort") {
                            FeedReplySortCard(
                                replyCount = viewModel.replyCount,
                                selected = selected,
                                updateSortReply = { index ->
                                    selected = index
                                    viewModel.listType = when (index) {
                                        0 -> "lastupdate_desc"
                                        1 -> "dateline_desc"
                                        2 -> "popular"
                                        else -> EMPTY_STRING
                                    }
                                    viewModel.fromFeedAuthor = if (index == 3) 1 else 0
                                    if (shouldShowSortCard)
                                        viewModel.isViewReply = true
                                    viewModel.refresh()
                                }
                            )
                            HorizontalDivider()
                        }

                        if (viewModel.listType == "lastupdate_desc") {
                            if (!response.topReplyRows.isNullOrEmpty()) {
                                item(key = "topReplyRows") {
                                    FeedReplyCard(
                                        data = response.topReplyRows[0],
                                        onViewUser = onViewUser,
                                        onShowTotalReply = { id, uid, frid ->
                                            openBottomSheet = true
                                            viewModel.replyId = id
                                            viewModel.uid = uid
                                            viewModel.frid = frid
                                            viewModel.fetchTotalReply()
                                        },
                                        onOpenLink = onOpenLink,
                                        onCopyText = onCopyText,
                                        onReport = onReport,
                                        onLike = { id, like, likeType ->
                                            viewModel.onLike(id, like, likeType)
                                        },
                                        onDelete = { id, deleteType ->
                                            viewModel.onDelete(id, deleteType)
                                        },
                                        onBlockUser = { uid ->
                                            viewModel.onBlockUser(uid)
                                        }
                                    )
                                    HorizontalDivider()
                                }
                            }

                            if (!response.replyMeRows.isNullOrEmpty()) {
                                item(key = "replyMeRows") {
                                    FeedReplyCard(
                                        data = response.replyMeRows[0],
                                        onViewUser = onViewUser,
                                        onShowTotalReply = { id, uid, frid ->
                                            openBottomSheet = true
                                            viewModel.replyId = id
                                            viewModel.uid = uid
                                            viewModel.frid = frid
                                            viewModel.fetchTotalReply()
                                        },
                                        onOpenLink = onOpenLink,
                                        onCopyText = onCopyText,
                                        onReport = onReport,
                                        onLike = { id, like, likeType ->
                                            viewModel.onLike(id, like, likeType)
                                        },
                                        onDelete = { id, deleteType ->
                                            viewModel.onDelete(id, deleteType)
                                        },
                                        onBlockUser = { uid ->
                                            viewModel.onBlockUser(uid)
                                        }
                                    )
                                    HorizontalDivider()
                                }
                            }
                        }
                        if (viewModel.isViewReply) {
                            viewModel.isViewReply = false
                            scope.launch {
                                lazyListState.scrollToItem(viewModel.itemSize)
                            }
                        }
                    }
                }

                if (viewModel.feedState is LoadingState.Success) {

                    ItemCard(
                        loadingState = viewModel.loadingState,
                        dataList = dataList,
                        loadMore = viewModel::loadMore,
                        isEnd = viewModel.isEnd,
                        onViewUser = onViewUser,
                        onViewFeed = onViewFeed,
                        onOpenLink = onOpenLink,
                        onCopyText = onCopyText,
                        onShowTotalReply = { id, uid, frid ->
                            openBottomSheet = true
                            viewModel.replyId = id
                            viewModel.uid = uid
                            viewModel.frid = frid
                            viewModel.fetchTotalReply()
                        },
                        onReport = onReport,
                        onViewFFFList = { _, _, _, _ -> },
                        onLike = { id, like, likeType ->
                            viewModel.onLike(id, like, likeType)
                        },
                        onDelete = { id, deleteType ->
                            viewModel.onDelete(id, deleteType)
                        },
                        onBlockUser = { uid ->
                            viewModel.onBlockUser(uid)
                        },
                        onFollowUser = { uid, isFollow ->
                            viewModel.onFollowUser(uid, isFollow)
                        }
                    )

                    FooterCard(
                        footerState = viewModel.footerState,
                        loadMore = viewModel::loadMore,
                        isFeed = true
                    )

                }

            }
            if (shouldShowSortCard) {
                FeedReplySortCard(
                    replyCount = viewModel.replyCount,
                    selected = selected,
                    updateSortReply = { index ->
                        selected = index
                        viewModel.listType = when (index) {
                            0 -> "lastupdate_desc"
                            1 -> "dateline_desc"
                            2 -> "popular"
                            else -> EMPTY_STRING
                        }
                        viewModel.fromFeedAuthor = if (index == 3) 1 else 0
                        viewModel.isViewReply = true
                        viewModel.refresh()
                    }
                )
            }

        }

    }

    if (openBottomSheet) {

        var reply: HomeFeedResponse.Data? =
            when (viewModel.frid ?: viewModel.replyId) {
                viewModel.topId ->
                    (viewModel.feedState as LoadingState.Success).response.topReplyRows?.getOrNull(0)

                viewModel.meId ->
                    (viewModel.feedState as LoadingState.Success).response.replyMeRows?.getOrNull(0)

                else ->
                    (viewModel.loadingState as LoadingState.Success).response.find {
                        it.id == (viewModel.frid ?: viewModel.replyId)
                    }
            }
        if (!viewModel.frid.isNullOrEmpty())
            reply = reply?.replyRows?.find { it.id == viewModel.replyId }

        ModalBottomSheet(
            onDismissRequest = {
                openBottomSheet = false
                viewModel.resetReplyState()
            },
            sheetState = bottomSheetState,
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                reply?.let {
                    item(key = "origin") {
                        FeedReplyCard(
                            data = reply,
                            isTotalReply = true,
                            isTopReply = true,
                            isReply2Reply = !viewModel.frid.isNullOrEmpty(),
                            onViewUser = onViewUser,
                            onShowTotalReply = { _, _, _ -> },
                            onOpenLink = onOpenLink,
                            onCopyText = {
                                context.copyText(it?.getAllLinkAndText?.richToString())
                            },
                            onReport = onReport,
                            onLike = { id, like, likeType ->
                                // viewModel.onLike(id, like, likeType)
                            },
                            onDelete = { id, deleteType ->
                                // viewModel.onDelete(id, deleteType)
                            },
                            onBlockUser = { uid ->
                                viewModel.onBlockUser(uid)
                            }
                        )
                        HorizontalDivider()
                    }
                }

                ItemCard(
                    loadingState = viewModel.replyLoadingState,
                    dataList = replyList,
                    loadMore = viewModel::loadMoreReply,
                    isEnd = viewModel.isEndReply,
                    onViewUser = onViewUser,
                    onViewFeed = onViewFeed,
                    onOpenLink = onOpenLink,
                    onCopyText = {
                        context.copyText(it?.getAllLinkAndText?.richToString())
                    },
                    onShowTotalReply = { _, _, _ -> },
                    onReport = onReport,
                    isTotalReply = true,
                    onViewFFFList = { _, _, _, _ -> },
                    onLike = { id, like, likeType ->
                        // viewModel.onLike(id, like, likeType)
                    },
                    onDelete = { id, deleteType ->
                        // viewModel.onDelete(id, deleteType)
                    },
                    onBlockUser = { uid ->
                        viewModel.onBlockUser(uid)
                    },
                    onFollowUser = { uid, isFollow ->
                        //viewModel.onFollowUser(uid, isFollow)
                    }
                )

                FooterCard(
                    footerState = viewModel.replyFooterState,
                    loadMore = viewModel::loadMoreReply,
                    isFeed = true
                )
            }
        }
    }

    viewModel.toastText?.let {
        viewModel.resetToastText()
        context.makeToast(it)
    }

}