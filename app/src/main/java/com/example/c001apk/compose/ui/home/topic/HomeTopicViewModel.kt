package com.example.c001apk.compose.ui.home.topic

import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.ui.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * Created by bggRGjQaUbCoE on 2024/6/11
 */
@HiltViewModel(assistedFactory = HomeTopicViewModel.ViewModelFactory::class)
class HomeTopicViewModel @AssistedInject constructor(
    @Assisted val url: String,
    private val networkRepo: NetworkRepo
) : BaseViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(url: String): HomeTopicViewModel
    }

    init {
        fetchData()
    }

    override suspend fun customFetchData() = networkRepo.getProductList(url)

}
