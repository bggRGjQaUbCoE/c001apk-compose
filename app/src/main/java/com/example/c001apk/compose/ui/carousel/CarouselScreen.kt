package com.example.c001apk.compose.ui.carousel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.model.TopicBean
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.CommonScreen
import com.example.c001apk.compose.ui.component.cards.LoadingCard
import com.example.c001apk.compose.util.ReportType
import com.example.c001apk.compose.util.decode
import com.example.c001apk.compose.util.makeToast
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/11
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselScreen(
    onBackClick: () -> Unit,
    url: String,
    title: String,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, Boolean) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
    onReport: (String, ReportType) -> Unit,
) {

    val viewModel =
        hiltViewModel<CarouselViewModel, CarouselViewModel.ViewModelFactory>(key = url + title) { factory ->
            factory.create(isInit = true, url = url.decode, title = title)
        }

    val layoutDirection = LocalLayoutDirection.current
    val scope = rememberCoroutineScope()
    var refreshState by remember { mutableStateOf(false) }

    var pagerState: PagerState

    val context = LocalContext.current
    viewModel.toastText?.let {
        viewModel.resetToastText()
        context.makeToast(it)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.systemBars
                    .only(WindowInsetsSides.Start + WindowInsetsSides.Top),
                navigationIcon = {
                    BackButton { onBackClick() }
                },
                title = {
                    Text(
                        text = viewModel.pageTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
            when (viewModel.loadingState) {
                LoadingState.Loading, LoadingState.Empty, is LoadingState.Error -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LoadingCard(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 10.dp),
                            state = viewModel.loadingState,
                            onClick = if (viewModel.loadingState is LoadingState.Loading) null
                            else viewModel::loadMore
                        )
                    }
                }

                is LoadingState.Success -> {
                    val dataList =
                        (viewModel.loadingState as LoadingState.Success<List<HomeFeedResponse.Data>>).response

                    val isIconTabLinkGridCard =
                        dataList.find { it.entityTemplate == "iconTabLinkGridCard" }
                    if (isIconTabLinkGridCard == null) {
                        HorizontalDivider()
                        CommonScreen(
                            viewModel = viewModel,
                            refreshState = null,
                            resetRefreshState = {},
                            paddingValues = PaddingValues(
                                start = paddingValues.calculateLeftPadding(layoutDirection),
                                bottom = paddingValues.calculateBottomPadding(),
                            ),
                            onViewUser = onViewUser,
                            onViewFeed = onViewFeed,
                            onOpenLink = onOpenLink,
                            onCopyText = onCopyText,
                            onReport = onReport,
                        )
                    } else {
                        isIconTabLinkGridCard.entities?.map {
                            TopicBean(it.url.orEmpty(), it.title.orEmpty())
                        }?.let { tabList ->
                            pagerState = rememberPagerState(pageCount = { tabList.size })
                            SecondaryScrollableTabRow(
                                modifier = Modifier.padding(
                                    start = paddingValues.calculateLeftPadding(layoutDirection),
                                ),
                                selectedTabIndex = pagerState.currentPage,
                                indicator = {
                                    TabRowDefaults.SecondaryIndicator(
                                        Modifier
                                            .tabIndicatorOffset(
                                                pagerState.currentPage,
                                                matchContentSize = true
                                            )
                                            .clip(
                                                RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                                            )
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
                                        text = { Text(text = tab.title) }
                                    )
                                }
                            }

                            HorizontalDivider()

                            HorizontalPager(
                                state = pagerState,
                            ) { index ->
                                CarouselContentScreen(
                                    url = tabList[index].url,
                                    title = tabList[index].title,
                                    paddingValues = PaddingValues(
                                        start = paddingValues.calculateLeftPadding(layoutDirection),
                                        bottom = paddingValues.calculateBottomPadding(),
                                    ),
                                    refreshState = refreshState,
                                    resetRefreshState = { refreshState = false },
                                    onViewUser = onViewUser,
                                    onViewFeed = onViewFeed,
                                    onOpenLink = onOpenLink,
                                    onCopyText = onCopyText,
                                    onReport = onReport,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}