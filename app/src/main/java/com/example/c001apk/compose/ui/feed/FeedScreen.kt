package com.example.c001apk.compose.ui.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.logic.model.FeedArticleContentBean
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.state.FooterState
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.NineImageView
import com.example.c001apk.compose.ui.component.cards.FeedArticleCard
import com.example.c001apk.compose.ui.component.cards.FeedBottomInfo
import com.example.c001apk.compose.ui.component.cards.FeedCard
import com.example.c001apk.compose.ui.component.cards.FeedHeader
import com.example.c001apk.compose.ui.component.cards.FeedReplyCard
import com.example.c001apk.compose.ui.component.cards.FeedRows
import com.example.c001apk.compose.ui.component.cards.LoadingCard
import com.example.c001apk.compose.util.ShareType
import com.example.c001apk.compose.util.copyText
import com.example.c001apk.compose.util.getShareText
import com.example.c001apk.compose.util.noRippleClickable
import com.example.c001apk.compose.util.shareText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/4
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onBackClick: () -> Unit,
    id: String,
    rid: String?,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
    onOpenLink: (String) -> Unit,
    onCopyText: (String?) -> Unit,
    onViewTopic: (String) -> Unit,
) {

    val viewModel =
        hiltViewModel<FeedViewModel, FeedViewModel.ViewModelFactory>(key = id + rid) { factory ->
            factory.create(id, rid)
        }

    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    val state = rememberPullToRefreshState()
    val lazyListState = rememberLazyListState()
    val firstVisibleItemIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
    var dropdownMenuExpanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(true)

    var openBottomSheet by remember { mutableStateOf(false) }

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
                                    lazyListState.scrollToItem(if (lazyListState.firstVisibleItemIndex > 0) 0 else 1)
                                }
                            },
                        contentAlignment = Alignment.CenterStart,

                        ) {
                        AnimatedVisibility(
                            visible = viewModel.feedState is LoadingState.Success && firstVisibleItemIndex <= 0,
                            enter = fadeIn(animationSpec = spring(stiffness = StiffnessLow)),
                            exit = fadeOut(animationSpec = spring(stiffness = StiffnessLow))
                        ) {
                            Text(
                                text = (viewModel.feedState as? LoadingState.Success)?.response?.feedTypeName
                                    ?: EMPTY_STRING,
                            )
                        }
                        AnimatedVisibility(
                            visible = viewModel.feedState is LoadingState.Success && firstVisibleItemIndex > 0,
                            enter = fadeIn(animationSpec = spring(stiffness = StiffnessLow)),
                            exit = fadeOut(animationSpec = spring(stiffness = StiffnessLow))
                        ) {
                            FeedHeader(
                                data = (viewModel.feedState as LoadingState.Success).response,
                                onViewUser = onViewUser,
                                isFeedContent = true,
                            )
                        }
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
                                listOf("Copy", "Share", "Fav", "Report", "Block")
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
            onRefresh = viewModel::refresh,
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
                        item {
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
                        if (response.messageRawOutput != "null") {

                            val listType =
                                object : TypeToken<List<FeedArticleContentBean?>?>() {}.type
                            val message: List<FeedArticleContentBean> =
                                Gson().fromJson(response.messageRawOutput, listType)

                            if (!response.messageCover.isNullOrEmpty()) {
                                item(key = "cover") {
                                    NineImageView(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                            .padding(top = 12.dp),
                                        pic = null,
                                        picArr = listOf(response.messageCover),
                                        feedType = null,
                                        isSingle = true
                                    )
                                }
                            }
                            if (!response.title.isNullOrEmpty()) {
                                item(key = "title") {
                                    Text(
                                        text = response.title,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                            .padding(top = 12.dp),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            items(message.filter {
                                it.type in listOf("text", "image", "shareUrl")
                            }.apply { viewModel.itemSize += this.size }) { item ->
                                FeedArticleCard(
                                    item = item,
                                    onOpenLink = onOpenLink,
                                    onCopyText = onCopyText,
                                )
                            }

                            item(key = "bottom") {
                                FeedBottomInfo(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .padding(bottom = if (response.targetRow == null && response.relationRows.isNullOrEmpty()) 12.dp else 0.dp),
                                    isFeedContent = true,
                                    ip = response.ipLocation.orEmpty(),
                                    dateline = response.dateline ?: 0,
                                    replyNum = response.replynum.orEmpty(),
                                    likeNum = response.likenum.orEmpty(),
                                )
                            }

                            item(key = "rows") {
                                FeedRows(
                                    modifier = Modifier.padding(bottom = 12.dp),
                                    isFeedContent = true,
                                    relationRows = response.relationRows,
                                    targetRow = response.targetRow,
                                    onOpenLink = onOpenLink
                                )
                            }

                            item {
                                HorizontalDivider()
                            }

                        } else {
                            item {
                                FeedCard(
                                    isFeedContent = true,
                                    data = response,
                                    onViewUser = onViewUser,
                                    onViewFeed = onViewFeed,
                                    onOpenLink = onOpenLink,
                                    onCopyText = onCopyText,
                                )
                            }

                            item {
                                HorizontalDivider()
                            }
                        }

                    }
                }

                if (viewModel.feedState is LoadingState.Success) {

                    when (viewModel.loadingState) {
                        LoadingState.Loading, LoadingState.Empty, is LoadingState.Error -> {
                            item {
                                Box(modifier = Modifier.fillParentMaxSize()) {
                                    LoadingCard(
                                        state = viewModel.loadingState,
                                        onClick = if (viewModel.loadingState is LoadingState.Loading) null
                                        else viewModel::loadMore,
                                        isFeed = true,
                                    )
                                }
                            }
                        }

                        is LoadingState.Success -> {
                            val dataList =
                                (viewModel.loadingState as LoadingState.Success<List<HomeFeedResponse.Data>>).response
                            itemsIndexed(dataList) { index, item ->
                                FeedReplyCard(
                                    data = item,
                                    onViewUser = onViewUser,
                                    showTotalReply = { id ->
                                        openBottomSheet = true
                                        viewModel.replyId = id
                                        viewModel.fetchTotalReply()
                                    },
                                    onOpenLink = onOpenLink,
                                    onCopyText = onCopyText,
                                )
                                HorizontalDivider()

                                if (index == dataList.lastIndex && !viewModel.isEnd) {
                                    viewModel.loadMore()
                                }
                            }
                        }
                    }

                    item {
                        LoadingCard(
                            state = viewModel.footerState,
                            onClick = if (viewModel.footerState is FooterState.Error) viewModel::loadMore
                            else null,
                            isFeed = true,
                        )
                    }

                }


            }
        }

    }

    if (openBottomSheet) {

        ModalBottomSheet(
            onDismissRequest = {
                openBottomSheet = false
                viewModel.resetReplyState()
            },
            sheetState = bottomSheetState,
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                when (viewModel.replyLoadingState) {
                    LoadingState.Loading, LoadingState.Empty, is LoadingState.Error -> {
                        item {
                            Box(modifier = Modifier.fillParentMaxSize()) {
                                LoadingCard(
                                    modifier = Modifier.align(Alignment.Center),
                                    state = viewModel.replyLoadingState,
                                    onClick = if (viewModel.replyLoadingState is LoadingState.Loading) null
                                    else viewModel::loadMoreReply,
                                    isFeed = true,
                                )
                            }
                        }
                    }

                    is LoadingState.Success -> {
                        val response =
                            (viewModel.replyLoadingState as LoadingState.Success).response
                        itemsIndexed(response) { index, item ->
                            FeedReplyCard(
                                data = item,
                                onViewUser = onViewUser,
                                showTotalReply = {

                                },
                                onOpenLink = onOpenLink,
                                onCopyText = {
                                    context.copyText(it)
                                },
                            )
                            HorizontalDivider()

                            if (index == response.lastIndex && !viewModel.isEndReply) {
                                viewModel.loadMoreReply()
                            }
                        }
                    }
                }

                item {
                    LoadingCard(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        state = viewModel.replyFooterState,
                        onClick = if (viewModel.replyFooterState is FooterState.Error) viewModel::loadMoreReply
                        else null
                    )
                }
            }
        }
    }

}