package com.example.c001apk.compose.ui.ffflist

import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by bggRGjQaUbCoE on 2024/6/12
 */
@HiltViewModel(assistedFactory = FFFContentViewModel.ViewModelFactory::class)
class FFFContentViewModel @AssistedInject constructor(
    @Assisted("url") val url: String,
    @Assisted("uid") val uid: String,
    private val networkRepo: NetworkRepo
) : BaseViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            @Assisted("url") url: String,
            @Assisted("uid") uid: String,
        ): FFFContentViewModel
    }

    init {
        fetchData()
    }

    override suspend fun customFetchData() =
        networkRepo.getFollowList(url, uid, page, lastItem)

}