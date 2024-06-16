package com.example.c001apk.compose.ui.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
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
 * Created by bggRGjQaUbCoE on 2024/6/4
 */
@HiltViewModel(assistedFactory = UserViewModel.ViewModelFactory::class)
class UserViewModel @AssistedInject constructor(
    @Assisted var uid: String,
    private val networkRepo: NetworkRepo,
    blackListRepo: BlackListRepo,
) : BaseViewModel(networkRepo, blackListRepo) {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(uid: String): UserViewModel
    }

    var userState by mutableStateOf<LoadingState<HomeFeedResponse.Data>>(LoadingState.Loading)
        private set

    init {
        fetchUserProfile()
    }

    lateinit var username: String

    private fun fetchUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getUserSpace(uid)
                .collect { state ->
                    userState = state
                    if (state is LoadingState.Success) {
                        uid = state.response.uid.orEmpty()
                        fetchData()
                    }
                    isRefreshing = false
                }
        }
    }

    override suspend fun customFetchData() = networkRepo.getUserFeed(uid, page, lastItem)

    var isPull = false
    override fun refresh() {
        if (!isRefreshing && !isLoadMore) {
            if (userState is LoadingState.Success) {
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
                userState = LoadingState.Loading
                fetchUserProfile()
            }
        }
    }

}