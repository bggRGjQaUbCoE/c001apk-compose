package com.example.c001apk.compose.ui.collection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.repository.BlackListRepo
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/21
 */
@HiltViewModel(assistedFactory = CollectionViewModel.ViewModelFactory::class)
class CollectionViewModel @AssistedInject constructor(
    @Assisted val id: String,
    networkRepo: NetworkRepo,
    blackListRepo: BlackListRepo,
) : BaseViewModel(networkRepo, blackListRepo) {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(id: String): CollectionViewModel
    }

    override suspend fun customFetchData() =
        networkRepo.getFollowList("/v6/collection/itemList", null, id, null, page, lastItem)

    init {
        fetchInfo()
    }

    var collectionState by mutableStateOf<LoadingState<HomeFeedResponse.Data>>(LoadingState.Loading)
        private set

    var title by mutableStateOf(EMPTY_STRING)

    var isPull = false
    override fun refresh() {
        if (!isRefreshing && !isLoadMore) {
            if (collectionState is LoadingState.Success) {
                page = 1
                isEnd = false
                isLoadMore = false
                isRefreshing = true
                firstItem = null
                lastItem = null
                fetchData()
            } else {
                if (isPull) {
                    isPull = false
                    viewModelScope.launch {
                        isRefreshing = true
                        delay(50)
                        isRefreshing = false
                    }
                }
                collectionState = LoadingState.Loading
                fetchInfo()
            }
        }
    }

    private fun fetchInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getFeedContent("/v6/collection/detail?id=$id")
                .collect { state ->
                    collectionState = state
                    if (state is LoadingState.Success) {
                        title = state.response.title.orEmpty()
                        fetchData()
                    }
                    isRefreshing = false
                }
        }
    }

}