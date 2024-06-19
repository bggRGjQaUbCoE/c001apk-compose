package com.example.c001apk.compose.ui.dyh

import com.example.c001apk.compose.logic.model.HomeFeedResponse
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
@HiltViewModel(assistedFactory = DyhContentViewModel.ViewModelFactory::class)
class DyhContentViewModel @AssistedInject constructor(
    @Assisted("id") val id: String,
    @Assisted("type") val type: String,
    private val networkRepo: NetworkRepo,
    blackListRepo: BlackListRepo,
) : BaseViewModel(networkRepo, blackListRepo) {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            @Assisted("id") id: String,
            @Assisted("type") type: String,
        ): DyhContentViewModel
    }

    init {
        fetchData()
    }

    override suspend fun customFetchData() = networkRepo.getDyhDetail(id, type, page, lastItem)

    override fun handleLoadMore(response: List<HomeFeedResponse.Data>): List<HomeFeedResponse.Data> {
        return response.distinctBy { it.entityId }
    }

}