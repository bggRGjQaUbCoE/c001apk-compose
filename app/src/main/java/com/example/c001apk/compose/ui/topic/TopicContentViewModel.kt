package com.example.c001apk.compose.ui.topic

import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.repository.BlackListRepo
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.ui.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * Created by bggRGjQaUbCoE on 2024/6/9
 */
@HiltViewModel(assistedFactory = TopicContentViewModel.ViewModelFactory::class)
class TopicContentViewModel @AssistedInject constructor(
    @Assisted("url") var url: String,
    @Assisted("title") var title: String,
    networkRepo: NetworkRepo,
    blackListRepo: BlackListRepo,
) : BaseViewModel(networkRepo, blackListRepo) {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            @Assisted("url") url: String,
            @Assisted("title") title: String,
        ): TopicContentViewModel
    }

    var sortType = ProductSortType.REPLY

    init {
        fetchData()
    }

    override suspend fun customFetchData() =
        networkRepo.getDataList(url, title, EMPTY_STRING, lastItem, page)

    override fun handleLoadMore(response: List<HomeFeedResponse.Data>): List<HomeFeedResponse.Data> {
        return response.distinctBy { it.entityId }
    }

}