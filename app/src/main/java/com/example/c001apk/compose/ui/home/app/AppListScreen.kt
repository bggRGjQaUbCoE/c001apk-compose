package com.example.c001apk.compose.ui.home.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.c001apk.compose.logic.model.UpdateCheckItem
import com.example.c001apk.compose.logic.providable.LocalUserPreferences
import com.example.c001apk.compose.util.isScrollingUp
import com.example.c001apk.compose.util.longVersionCodeCompat

/**
 * Created by bggRGjQaUbCoE on 2024/6/9
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    refreshState: Boolean,
    resetRefreshState: () -> Unit,
    paddingValues: PaddingValues,
    onViewApp: (String) -> Unit,
    onCheckUpdate: (List<UpdateCheckItem>) -> Unit
) {

    val viewModel = hiltViewModel<AppListViewModel>()
    val prefs = LocalUserPreferences.current
    val context = LocalContext.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(refreshState) {
        if (refreshState) {
            resetRefreshState()
            if (view.isVisible) {
                viewModel.refresh()
                lazyListState.scrollToItem(0)
            }
        }
    }

    PullToRefreshBox(
        modifier = Modifier.padding(paddingValues),
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
            state = lazyListState,
        ) {

            items(
                items = viewModel.appList,
                key = { it.packageInfo.packageName }
            ) {
                ListItem(
                    modifier = Modifier.clickable {
                        onViewApp(it.packageInfo.packageName)
                    },
                    leadingContent = {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(it.packageInfo)
                                .crossfade(true)
                                .build(),
                            contentDescription = it.label,
                            modifier = Modifier
                                .padding(4.dp)
                                .width(48.dp)
                                .height(48.dp)
                        )
                    },
                    headlineContent = {
                        Text(text = it.label)
                    },
                    supportingContent = {
                        Text(
                            text =
                            "${it.packageInfo.packageName}\n${it.packageInfo.versionName}(${it.packageInfo.longVersionCodeCompat})"
                        )
                    }
                )
            }

        }

        if (viewModel.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                strokeCap = StrokeCap.Round
            )
        }

        if (prefs.checkUpdate && viewModel.appList.isNotEmpty()) {
            AnimatedVisibility(
                visible = lazyListState.isScrollingUp(),
                enter = slideInVertically { it * 2 },
                exit = slideOutVertically { it * 2 },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 25.dp, bottom = 25.dp)
            ) {
                FloatingActionButton(
                    onClick = { onCheckUpdate(viewModel.dataList) },
                ) {
                    Icon(imageVector = Icons.Default.Update, contentDescription = null)
                }
            }
        }

    }

}