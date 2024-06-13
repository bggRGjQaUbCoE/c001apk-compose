package com.example.c001apk.compose.ui.topic

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
    private val networkRepo: NetworkRepo
) : BaseViewModel() {

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

    override suspend fun customFetchData() = networkRepo.getDataList(url, title, "", lastItem, page)

}