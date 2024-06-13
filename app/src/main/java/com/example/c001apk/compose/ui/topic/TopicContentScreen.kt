package com.example.c001apk.compose.ui.topic

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.ui.component.CommonScreen
import com.example.c001apk.compose.util.ReportType

/**
 * Created by bggRGjQaUbCoE on 2024/6/9
 */
@Composable
fun TopicContentScreen(
    refreshState: Boolean,
    resetRefreshState: () -> Unit,
    id: String?,
    url: String,
    title: String,
    sortType: ProductSortType,
    paddingValues: PaddingValues,
    onViewUser: (String) -> Unit,
    onViewFeed: (String) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
    onReport: (String, ReportType) -> Unit,
) {

    val viewModel =
        hiltViewModel<TopicContentViewModel, TopicContentViewModel.ViewModelFactory>(key = title) { factory ->
            factory.create(url, title)
        }

    if (!id.isNullOrEmpty() && title == "讨论") {
        LaunchedEffect(sortType) {
            if (sortType != viewModel.sortType) {
                viewModel.sortType = sortType
                viewModel.title = when (sortType) {
                    ProductSortType.REPLY -> "最近回复"
                    ProductSortType.HOT -> "热度排序"
                    ProductSortType.DATELINE -> "最新发布"
                }
                viewModel.url = "/page?url=/product/feedList?type=feed&id=$id&" + when (sortType) {
                    ProductSortType.REPLY -> "ignoreEntityById=1"
                    ProductSortType.HOT -> "listType=rank_score"
                    ProductSortType.DATELINE -> "ignoreEntityById=1&listType=dateline_desc"
                }
                viewModel.refresh()
            }
        }
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
    )

}