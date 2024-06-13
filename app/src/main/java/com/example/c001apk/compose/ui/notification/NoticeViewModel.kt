package com.example.c001apk.compose.ui.notification

import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.ui.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * Created by bggRGjQaUbCoE on 2024/6/13
 */
@HiltViewModel(assistedFactory = NoticeViewModel.ViewModelFactory::class)
class NoticeViewModel @AssistedInject constructor(
    @Assisted val url: String,
    private val networkRepo: NetworkRepo
) : BaseViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(url: String): NoticeViewModel
    }

    init {
        fetchData()
    }

    override suspend fun customFetchData() = networkRepo.getMessage(url, page, lastItem)

}