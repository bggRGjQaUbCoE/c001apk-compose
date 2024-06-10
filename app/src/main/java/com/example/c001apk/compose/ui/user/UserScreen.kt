package com.example.c001apk.compose.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.FooterCard
import com.example.c001apk.compose.ui.component.ItemCard
import com.example.c001apk.compose.ui.component.cards.LoadingCard
import com.example.c001apk.compose.ui.component.cards.UserInfoCard
import com.example.c001apk.compose.util.DateUtils.timeStamp2Date
import com.example.c001apk.compose.util.ShareType
import com.example.c001apk.compose.util.copyText
import com.example.c001apk.compose.util.getShareText
import com.example.c001apk.compose.util.shareText

/**
 * Created by bggRGjQaUbCoE on 2024/6/4
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    uid: String,
    onBackClick: () -> Unit,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
    onOpenLink: (String) -> Unit,
    onCopyText: (String?) -> Unit,
    onSearch: (String, String, String) -> Unit,
) {

    val layoutDirection = LocalLayoutDirection.current

    val context = LocalContext.current
    val state = rememberPullToRefreshState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val viewModel = hiltViewModel<UserViewModel, UserViewModel.ViewModelFactory> { factory ->
        factory.create(uid)
    }
    var showUserInfoDialog by remember { mutableStateOf(false) }
    var dropdownMenuExpanded by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val firstVisibleItemIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton { onBackClick() }
                },
                title = {
                    Text(
                        text = if (firstVisibleItemIndex > 0)
                            (viewModel.userState as? LoadingState.Success)?.response?.username
                                ?: uid
                        else EMPTY_STRING,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    if (viewModel.userState is LoadingState.Success) {
                        Row(Modifier.wrapContentSize(Alignment.TopEnd)) {
                            IconButton(
                                onClick = {
                                    onSearch(viewModel.username, "user", viewModel.uid)
                                }
                            ) {
                                Icon(Icons.Default.Search, contentDescription = null)
                            }
                            Box {
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
                                    listOf("Copy", "Share", "Report", "Block", "User Info")
                                        .forEachIndexed { index, menu ->
                                            DropdownMenuItem(
                                                text = { Text(menu) },
                                                onClick = {
                                                    dropdownMenuExpanded = false
                                                    when (index) {
                                                        0 -> context.copyText(
                                                            getShareText(ShareType.USER, uid)
                                                        )

                                                        1 -> context.shareText(
                                                            getShareText(ShareType.USER, uid)
                                                        )

                                                        4 -> showUserInfoDialog = true
                                                    }
                                                }
                                            )
                                        }
                                }
                            }

                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->

        PullToRefreshBox(
            modifier = Modifier.padding(
                start = paddingValues.calculateLeftPadding(layoutDirection),
                end = paddingValues.calculateRightPadding(layoutDirection),
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
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                state = lazyListState
            ) {

                when (viewModel.userState) {
                    LoadingState.Loading, LoadingState.Empty, is LoadingState.Error -> {
                        item {
                            Box(modifier = Modifier.fillParentMaxSize()) {
                                LoadingCard(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(horizontal = 20.dp),
                                    state = viewModel.userState,
                                    onClick = if (viewModel.userState is LoadingState.Loading) null
                                    else viewModel::refresh
                                )
                            }
                        }
                    }

                    is LoadingState.Success -> {
                        val response = (viewModel.userState as LoadingState.Success).response
                        viewModel.username = response.username.orEmpty()
                        item {
                            UserInfoCard(
                                data = response,
                                onFollow = {

                                },
                                onPMUser = {

                                },
                                onViewFFFList = { uid, type ->

                                }
                            )
                        }
                    }
                }

                if (viewModel.userState is LoadingState.Success) {

                    ItemCard(
                        loadingState = viewModel.loadingState,
                        loadMore = viewModel::loadMore,
                        isEnd = viewModel.isEnd,
                        onViewUser = onViewUser,
                        onViewFeed = onViewFeed,
                        onOpenLink = onOpenLink,
                        onCopyText = onCopyText,
                        onShowTotalReply = {},
                    )

                    FooterCard(
                        footerState = viewModel.footerState,
                        loadMore = viewModel::loadMore,
                    )
                }

            }
        }


    }

    when {
        showUserInfoDialog -> {
            val data = (viewModel.userState as LoadingState.Success).response
            AlertDialog(
                onDismissRequest = { showUserInfoDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = { showUserInfoDialog = false }) {
                        Text(text = "OK")
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = data.username.orEmpty(),
                    )
                },
                text = {
                    Text(
                        text = """
                                uid: ${data.uid}
                                
                                sex: Lv.${data.level}
                                
                                gender: ${if (data.gender == 0) "female" else if (data.gender == 1) "male" else "unknown"}
                                
                                reg-duration: ${((System.currentTimeMillis() / 1000 - (data.regdate ?: 0)) / 24 / 3600)} days
                                
                                reg-time: ${timeStamp2Date(data.regdate ?: 0)}
                            """.trimIndent()
                    )
                },
            )
        }
    }

}