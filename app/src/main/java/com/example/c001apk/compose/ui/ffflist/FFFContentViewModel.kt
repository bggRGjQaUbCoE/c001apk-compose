package com.example.c001apk.compose.ui.ffflist

import com.example.c001apk.compose.logic.repository.BlackListRepo
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.ui.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * Created by bggRGjQaUbCoE on 2024/6/12
 */
@HiltViewModel(assistedFactory = FFFContentViewModel.ViewModelFactory::class)
class FFFContentViewModel @AssistedInject constructor(
    @Assisted("url") val url: String,
    @Assisted("uid") val uid: String?,
    @Assisted("id") val id: String?,
    @Assisted val showDefault: Int?,
    private val networkRepo: NetworkRepo,
    blackListRepo: BlackListRepo,
) : BaseViewModel(networkRepo, blackListRepo) {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            @Assisted("url") url: String,
            @Assisted("uid") uid: String?,
            @Assisted("id") id: String?,
            @Assisted showDefault: Int?,
        ): FFFContentViewModel
    }

    init {
        fetchData()
    }

    override suspend fun customFetchData() =
        networkRepo.getFollowList(url, uid, id, showDefault, page, lastItem)

}