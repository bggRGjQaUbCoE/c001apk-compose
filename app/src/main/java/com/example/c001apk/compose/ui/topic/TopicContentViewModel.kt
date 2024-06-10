package com.example.c001apk.compose.ui.topic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.constant.Constants.entityTypeList
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.logic.state.FooterState
import com.example.c001apk.compose.logic.state.LoadingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/9
 */
@HiltViewModel(assistedFactory = TopicContentViewModel.ViewModelFactory::class)
class TopicContentViewModel @AssistedInject constructor(
    @Assisted("url") val url: String,
    @Assisted("title") val title: String,
    private val networkRepo: NetworkRepo
) : ViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            @Assisted("url") url: String,
            @Assisted("title") title: String,
        ): TopicContentViewModel
    }

    var isRefreshing by mutableStateOf(false)
        private set

    var loadingState by mutableStateOf<LoadingState<List<HomeFeedResponse.Data>>>(LoadingState.Loading)
        private set
    var footerState by mutableStateOf<FooterState>(FooterState.Success)
        private set

    private var page = 1
    private var isLoadMore = false
    var isEnd = false
    private var lastItem: String? = null

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getDataList(url, title, "", lastItem, page)
                .collect { result ->
                    when (result) {
                        LoadingState.Empty -> {
                            if (loadingState is LoadingState.Success)
                                footerState = FooterState.End
                            else
                                loadingState = result
                            isEnd = true
                        }

                        is LoadingState.Error -> {
                            if (loadingState is LoadingState.Success)
                                footerState = FooterState.Error(result.errMsg)
                            else
                                loadingState = result
                            isEnd = true
                        }

                        LoadingState.Loading -> {
                            if (loadingState is LoadingState.Success)
                                footerState = FooterState.Loading
                            else
                                loadingState = result
                        }

                        is LoadingState.Success -> {
                            page++
                            val response = result.response.filter {
                                it.entityType in entityTypeList
                            } // TODO
                            lastItem = response.lastOrNull()?.id
                            loadingState =
                                if (isLoadMore)
                                    LoadingState.Success(
                                        (((loadingState as? LoadingState.Success)?.response
                                            ?: emptyList()) + response).distinctBy { it.entityId }
                                    )
                                else
                                    LoadingState.Success(response)
                            footerState = FooterState.Success
                        }
                    }
                    isLoadMore = false
                    isRefreshing = false
                }
        }
    }

    fun refresh() {
        if (!isRefreshing && !isLoadMore) {
            page = 1
            isEnd = false
            isLoadMore = false
            isRefreshing = true
            fetchData()
        }
    }

    fun loadMore() {
        if (!isRefreshing && !isLoadMore) {
            isEnd = false
            isLoadMore = true
            fetchData()
            if (loadingState is LoadingState.Success) {
                footerState = FooterState.Loading
            } else {
                loadingState = LoadingState.Loading
            }
        }
    }

}