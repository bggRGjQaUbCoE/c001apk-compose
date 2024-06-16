package com.example.c001apk.compose.ui.app

import com.example.c001apk.compose.logic.repository.BlackListRepo
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.ui.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * Created by bggRGjQaUbCoE on 2024/6/10
 */
@HiltViewModel(assistedFactory = AppContentViewModel.ViewModelFactory::class)
class AppContentViewModel @AssistedInject constructor(
    @Assisted("url") val url: String,
    @Assisted("appCommentTitle") val appCommentTitle: String,
    private val networkRepo: NetworkRepo,
    blackListRepo: BlackListRepo,
) : BaseViewModel(networkRepo, blackListRepo) {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            @Assisted("url") url: String,
            @Assisted("appCommentTitle") appCommentTitle: String,
        ): AppContentViewModel
    }

    init {
        fetchData()
    }

    override suspend fun customFetchData() =
        networkRepo.getDataList(
            url,
            appCommentTitle,
            null,
            lastItem,
            page
        )

}