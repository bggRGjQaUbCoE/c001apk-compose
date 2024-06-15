package com.example.c001apk.compose.ui.home.feed

import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.ui.base.BaseViewModel
import com.example.c001apk.compose.ui.home.TabType
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

}