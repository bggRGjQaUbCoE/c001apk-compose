package com.example.c001apk.compose.ui.app

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
    @Assisted("id") val id: String,
    @Assisted("appCommentSort") val appCommentSort: String,
    @Assisted("appCommentTitle") val appCommentTitle: String,
    private val networkRepo: NetworkRepo
) : BaseViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            @Assisted("id") id: String,
            @Assisted("appCommentSort") appCommentSort: String,
            @Assisted("appCommentTitle") appCommentTitle: String,
        ): AppContentViewModel
    }

    override suspend fun customFetchData() =
        networkRepo.getDataList(
            "/page?url=/feed/apkCommentList?id=$id$appCommentSort",
            appCommentTitle,
            null,
            lastItem,
            page
        )

}