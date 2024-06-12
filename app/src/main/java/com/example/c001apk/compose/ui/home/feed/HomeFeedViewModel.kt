package com.example.c001apk.compose.ui.home.feed

import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.ui.base.BaseViewModel
import com.example.c001apk.compose.ui.home.TabType
import com.example.c001apk.compose.util.PrefManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * Created by bggRGjQaUbCoE on 2024/6/3
 */
@HiltViewModel(assistedFactory = HomeFeedViewModel.ViewModelFactory::class)
class HomeFeedViewModel @AssistedInject constructor(
    @Assisted val type: TabType,
    @Assisted("dataListUrl") var dataListUrl: String,
    @Assisted("dataListTitle") var dataListTitle: String,
    @Assisted("installTime") val installTime: String,
    private val networkRepo: NetworkRepo,
) : BaseViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            type: TabType,
            @Assisted("dataListUrl") dataListUrl: String,
            @Assisted("dataListTitle") dataListTitle: String,
            @Assisted("installTime") installTime: String,
        ): HomeFeedViewModel
    }

    init {
        fetchData()
    }

    override suspend fun customFetchData() = when (type) {
        TabType.FOLLOW, TabType.HOT, TabType.COOLPIC ->
            networkRepo.getDataList(dataListUrl, dataListTitle, null, lastItem, page)

        TabType.FEED -> networkRepo.getHomeFeed(page, firstLaunch, installTime, null, null)

        else -> throw IllegalArgumentException("invalid type: ${type.name}")
    }

    private fun initParams() {
        when (type) {
            TabType.HOT -> {
                dataListUrl = "/page?url=V9_HOME_TAB_RANKING"
                dataListTitle = "热榜"
            }

            TabType.FOLLOW -> {
                when (PrefManager.followType) {
                    "all" -> {
                        dataListUrl = "/page?url=V9_HOME_TAB_FOLLOW"
                        dataListTitle = "全部关注"
                    }

                    "circle" -> {
                        dataListUrl = "/page?url=V9_HOME_TAB_FOLLOW&type=circle"
                        dataListTitle = "好友关注"
                    }

                    "topic" -> {
                        dataListUrl = "/page?url=V9_HOME_TAB_FOLLOW&type=topic"
                        dataListTitle = "话题关注"
                    }

                    else -> {
                        dataListUrl = "/page?url=V9_HOME_TAB_FOLLOW&type=product"
                        dataListTitle = "数码关注"
                    }
                }
            }

            TabType.COOLPIC -> {
                dataListUrl = "/page?url=V11_FIND_COOLPIC"
                dataListTitle = "酷图"
            }

            else -> {}
        }
    }

}