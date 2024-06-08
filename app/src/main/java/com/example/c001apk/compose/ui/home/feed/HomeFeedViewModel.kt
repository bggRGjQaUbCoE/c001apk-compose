package com.example.c001apk.compose.ui.home.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.logic.state.FooterState
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.home.TabType
import com.example.c001apk.compose.util.PrefManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/3
 */
@HiltViewModel(assistedFactory = HomeFeedViewModel.ViewModelFactory::class)
class HomeFeedViewModel @AssistedInject constructor(
    @Assisted val type: TabType,
    @Assisted val installTime: String,
    private val networkRepo: NetworkRepo,
    // private val blackListRepo: BlackListRepo,
) : ViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(type: TabType, installTime: String): HomeFeedViewModel
    }

    private lateinit var dataListUrl: String
    private lateinit var dataListTitle: String

    private val entityTypeList = listOf("feed")
    private val entityTemplateList =
        listOf(
            "imageCarouselCard_1", "iconLinkGridCard", "iconMiniScrollCard", "iconMiniGridCard",
            "imageSquareScrollCard", "titleCard", "iconScrollCard", "imageTextScrollCard"
        )

    init {
        when (type) {
            TabType.HOT -> {
                dataListUrl = "/page?url=V9_HOME_TAB_RANKING"
                dataListTitle = "热榜"
            }

            TabType.FOLLOW -> {
                when (PrefManager.followType) {
                    "all" -> {
                        dataListUrl = "/page?url=V9_HOME_TAB_FOLLOW"
                        dataListTitle = "全部关注"
                    }

                    "circle" -> {
                        dataListUrl = "/page?url=V9_HOME_TAB_FOLLOW&type=circle"
                        dataListTitle = "好友关注"
                    }

                    "topic" -> {
                        dataListUrl = "/page?url=V9_HOME_TAB_FOLLOW&type=topic"
                        dataListTitle = "话题关注"
                    }

                    else -> {
                        dataListUrl = "/page?url=V9_HOME_TAB_FOLLOW&type=product"
                        dataListTitle = "数码关注"
                    }
                }
            }

            TabType.COOLPIC -> {
                dataListUrl = "/page?url=V11_FIND_COOLPIC"
                dataListTitle = "酷图"
            }

            else -> {}
        }
        fetchData()
    }

    private var page = 1
    private var firstLaunch = 1
    private var isLoadMore = false
    var isEnd = false
    private var firstItem: String? = null
    private var lastItem: String? = null

    /*private val _loadingState =
         MutableStateFlow<LoadingState<List<HomeFeedResponse.Data>>>(LoadingState.Loading)
     val loadingState = _loadingState.asStateFlow()*/

    /*private val _footerState = MutableStateFlow<FooterState>(FooterState.Success)
     val footerState = _footerState.asStateFlow()*/

    var loadingState by mutableStateOf<LoadingState<List<HomeFeedResponse.Data>>>(LoadingState.Loading)
        private set

    var footerState by mutableStateOf<FooterState>(FooterState.Success)
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    private fun fetchHomeFeed() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getHomeFeed(page, firstLaunch, installTime, null, null)
                .onStart {
                    if (firstLaunch == 1)
                        firstLaunch = 0
                }
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
                                it.entityType in entityTypeList || it.entityTemplate in entityTemplateList
                            } // TODO
                            firstItem = response.firstOrNull()?.id
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

    private fun fetchDataList() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getDataList(dataListUrl, dataListTitle, null, lastItem, page)
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
                                it.entityType in entityTypeList || it.entityTemplate in entityTemplateList
                            } // TODO
                            firstItem = response.firstOrNull()?.id
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

    fun refresh() {
        if (!isRefreshing && !isLoadMore) {
            page = 1
            isEnd = false
            isLoadMore = false
            isRefreshing = true
            fetchData()
        }
    }

    private fun fetchData() {
        when (type) {
            TabType.FEED -> fetchHomeFeed()
            TabType.FOLLOW, TabType.HOT, TabType.COOLPIC -> fetchDataList()
            else -> throw IllegalArgumentException("invalid type: $type")
        }
    }

}