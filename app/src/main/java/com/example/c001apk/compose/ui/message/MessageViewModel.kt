package com.example.c001apk.compose.ui.message

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.constant.Constants
import com.example.c001apk.compose.logic.repository.BlackListRepo
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.logic.repository.UserPreferencesRepository
import com.example.c001apk.compose.logic.state.FooterState
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.base.BaseViewModel
import com.example.c001apk.compose.util.CookieUtil.atcommentme
import com.example.c001apk.compose.util.CookieUtil.atme
import com.example.c001apk.compose.util.CookieUtil.contacts_follow
import com.example.c001apk.compose.util.CookieUtil.feedlike
import com.example.c001apk.compose.util.CookieUtil.isLogin
import com.example.c001apk.compose.util.CookieUtil.message
import com.example.c001apk.compose.util.CookieUtil.uid
import com.example.c001apk.compose.util.encode
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/11
 */
@HiltViewModel(assistedFactory = MessageViewModel.ViewModelFactory::class)
class MessageViewModel @AssistedInject constructor(
    @Assisted val url: String,
    networkRepo: NetworkRepo,
    blackListRepo: BlackListRepo,
    private val userPreferencesRepository: UserPreferencesRepository,
) : BaseViewModel(networkRepo, blackListRepo) {

    var fffList by mutableStateOf<List<String>>(emptyList())
        private set
    var badgeList by mutableStateOf(listOf(atme, atcommentme, feedlike, contacts_follow, message))
        private set

    init {
        loadingState = LoadingState.Success(emptyList())
    }

    @AssistedFactory
    interface ViewModelFactory {
        fun create(url: String): MessageViewModel
    }

    override suspend fun customFetchData() =
        networkRepo.getMessage(url, page, lastItem)

    private fun fetchProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getProfile(uid)
                .collect { result ->
                    val data = result.getOrNull()
                    if (data?.data != null) {
                        fffList = listOf(
                            data.data.feed?.id ?: "0",
                            data.data.follow ?: "0",
                            data.data.fans ?: "0"
                        )
                        userPreferencesRepository.apply {
                            setUserAvatar(data.data.userAvatar.orEmpty())
                            setUsername(data.data.username.encode)
                            setLevel(data.data.level ?: "0")
                            setExperience(data.data.experience ?: "0")
                            setNextLevelExperience(data.data.nextLevelExperience ?: "0")
                        }
                    } else {
                        result.exceptionOrNull()?.printStackTrace()
                    }
                }
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            userPreferencesRepository.apply {
                setUid(Constants.EMPTY_STRING)
                setUserAvatar(Constants.EMPTY_STRING)
                setUsername(Constants.EMPTY_STRING)
                setToken(Constants.EMPTY_STRING)
                setIsLogin(false)
            }
            fffList = emptyList()
            badgeList = listOf(null)
            loadingState = LoadingState.Success(emptyList())
            footerState = FooterState.Success
        }
    }

    private fun onCheckCount() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.checkCount()
                .collect { result ->
                    result.getOrNull()?.data?.let {
                        badgeList = listOf(
                            it.atme, it.atcommentme, it.feedlike, it.contactsFollow, it.message
                        )
                    }
                }
        }
    }

    override fun refresh() {
        if (isLogin && !isRefreshing && !isLoadMore) {
            page = 1
            isEnd = false
            isLoadMore = false
            isRefreshing = true
            firstItem = null
            lastItem = null
            fetchProfile()
            onCheckCount()
            fetchData()
        } else {
            viewModelScope.launch {
                isRefreshing = true
                delay(50)
                isRefreshing = false
            }
        }
    }

    fun clearBadge(index: Int) {
        badgeList = badgeList.mapIndexed { i, count ->
            if (index == i) null
            else count
        }
    }

    lateinit var deleteId: String
    fun onPostDelete() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.postDelete("/v6/notification/delete", deleteId)
                .collect { result ->
                    val data = result.getOrNull()
                    if (data != null) {
                        if (!data.message.isNullOrEmpty()) {
                            toastText = data.message
                        } else if (data.data?.count?.contains("成功") == true) {
                            var response = (loadingState as LoadingState.Success).response
                            response = response.filterNot { it.id == deleteId }
                            loadingState = LoadingState.Success(response)
                            toastText = data.data.count
                        }
                    } else {
                        toastText = result.exceptionOrNull()?.message ?: "response is null"
                        result.exceptionOrNull()?.printStackTrace()
                    }
                }
        }
    }

}