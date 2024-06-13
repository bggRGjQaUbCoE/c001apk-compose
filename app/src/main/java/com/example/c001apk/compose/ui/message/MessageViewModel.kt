package com.example.c001apk.compose.ui.message

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.constant.Constants
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by bggRGjQaUbCoE on 2024/6/11
 */
@HiltViewModel
class MessageViewModel @Inject constructor(
    private val networkRepo: NetworkRepo,
    private val userPreferencesRepository: UserPreferencesRepository
) : BaseViewModel() {

    var fffList by mutableStateOf<List<String>>(emptyList())
    var badgeList by mutableStateOf(listOf(atme, atcommentme, feedlike, contacts_follow, message))

    init {
        loadingState = LoadingState.Success(emptyList())
    }

    override suspend fun customFetchData() =
        networkRepo.getMessage("/v6/notification/list", page, lastItem)

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
                    val response = result.getOrNull()
                    response?.data?.let {
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

}