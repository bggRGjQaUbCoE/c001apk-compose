package com.example.c001apk.compose.ui.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.cards.AppInfoCard
import com.example.c001apk.compose.ui.component.cards.LoadingCard
import com.example.c001apk.compose.util.Utils.downloadApk
import com.example.c001apk.compose.util.copyText
import com.example.c001apk.compose.util.makeToast
import com.example.c001apk.compose.util.openInBrowser
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/10
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    onBackClick: () -> Unit,
    packageName: String,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
    onOpenLink: (String) -> Unit,
    onCopyText: (String?) -> Unit,
    onSearch: (String, String, String) -> Unit,
) {

    val viewModel =
        hiltViewModel<AppViewModel, AppViewModel.ViewModelFactory> { factory ->
            factory.create(packageName)
        }

    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current

    val tabList by lazy { listOf("最近回复", "最新发布", "热度排序") }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var dropdownMenuExpanded by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val firstVisibleItemIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = {
            tabList.size
        }
    )
    val scope = rememberCoroutineScope()
    var refreshState by remember { mutableStateOf(false) }

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
                            (viewModel.appState as? LoadingState.Success)?.response?.title
                                ?: packageName
                        else EMPTY_STRING,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    if (viewModel.appState is LoadingState.Success) {
                        Row(Modifier.wrapContentSize(Alignment.TopEnd)) {
                            IconButton(
                                onClick = {
                                    onSearch(viewModel.title, "apk", viewModel.id)
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
                                    listOf("Follow", "Block")
                                        .forEachIndexed { index, menu ->
                                            DropdownMenuItem(
                                                text = { Text(menu) },
                                                onClick = {
                                                    dropdownMenuExpanded = false
                                                    when (index) {

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = paddingValues.calculateLeftPadding(layoutDirection),
                    end = paddingValues.calculateRightPadding(layoutDirection),
                )
        ) {

            when (viewModel.appState) {
                LoadingState.Loading, LoadingState.Empty, is LoadingState.Error -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LoadingCard(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 20.dp),
                            state = viewModel.appState,
                            onClick = if (viewModel.appState is LoadingState.Loading) null
                            else viewModel::refresh
                        )
                    }
                }

                is LoadingState.Success -> {
                    val response = (viewModel.appState as LoadingState.Success).response
                    viewModel.id = response.id.orEmpty()
                    viewModel.title = response.title.orEmpty()
                    viewModel.versionName = response.apkversionname.orEmpty()
                    viewModel.versionCode = response.apkversioncode.orEmpty()
                    AppInfoCard(
                        data = response,
                        onDownloadApk = viewModel::onGetDownloadLink
                    )

                    if (response.commentStatusText == "允许评论" || response.entityType == "appForum") {

                        SecondaryScrollableTabRow(
                            modifier = Modifier.fillMaxWidth(),
                            selectedTabIndex = pagerState.currentPage,
                            indicator = {
                                TabRowDefaults.SecondaryIndicator(
                                    Modifier
                                        .tabIndicatorOffset(
                                            pagerState.currentPage,
                                            matchContentSize = true
                                        )
                                        .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                                )
                            },
                            divider = {}
                        ) {
                            tabList.forEachIndexed { index, tab ->
                                Tab(
                                    selected = pagerState.currentPage == index,
                                    onClick = {
                                        if (pagerState.currentPage == index) {
                                            refreshState = true
                                        }
                                        scope.launch { pagerState.animateScrollToPage(index) }
                                    },
                                    text = { Text(text = tab) }
                                )
                            }
                        }

                        HorizontalDivider()

                        HorizontalPager(
                            state = pagerState
                        ) { index ->
                            AppContentScreen(
                                paddingValues = paddingValues,
                                refreshState = refreshState,
                                resetRefreshState = {
                                    refreshState = false
                                },
                                id = response.id.orEmpty(),
                                appCommentSort = when (index) {
                                    0 -> ""
                                    1 -> "&sort=dateline_desc"
                                    2 -> "&sort=popular"
                                    else -> ""

                                },
                                appCommentTitle = when (index) {
                                    0 -> "最近回复"
                                    1 -> "最新发布"
                                    2 -> "热度排序"
                                    else -> "最近回复"
                                },
                                onViewUser = onViewUser,
                                onViewFeed = onViewFeed,
                                onOpenLink = onOpenLink,
                                onCopyText = onCopyText,
                            )
                        }

                    } else {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(16.dp),
                            text = response.commentStatusText.orEmpty(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
    }

    when {
        viewModel.downloadApk -> {
            viewModel.reset()
            try {
                downloadApk(
                    context, viewModel.downloadUrl,
                    "${viewModel.title}-${viewModel.versionName}-${viewModel.versionCode}.apk"
                )
            } catch (e: Exception) {
                e.printStackTrace()
                try {
                    context.openInBrowser(viewModel.downloadUrl)
                } catch (e: Exception) {
                    context.makeToast("下载失败")
                    context.copyText(viewModel.downloadUrl)
                }
            }

        }
    }

}