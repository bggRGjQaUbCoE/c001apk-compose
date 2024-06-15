package com.example.c001apk.compose.ui.home.feed

import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.logic.repository.UserPreferencesRepository
import com.example.c001apk.compose.ui.base.BaseViewModel
import com.example.c001apk.compose.ui.home.TabType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/3
 */
@HiltViewModel(assistedFactory = HomeFeedViewModel.ViewModelFactory::class)
class HomeFeedViewModel @AssistedInject constructor(
    @Assisted val type: TabType,
    @Assisted("dataListUrl") var dataListUrl: String,
    @Assisted("dataListTitle") var dataListTitle: String,
    @Assisted("installTime") var installTime: String,
    private val networkRepo: NetworkRepo,
    private val userPreferencesRepository: UserPreferencesRepository
) : BaseViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            type: TabType,
            @Assisted("dataListUrl") dataListUrl: String,
            @Assisted("dataListTitle") dataListTitle: String,
            @Assisted("installTime") installTime: String,
        ): HomeFeedViewModel
    }

    init {
        if (installTime.isEmpty()) {
            installTime = System.currentTimeMillis().toString()
            setInstallTime()
        }
        fetchData()
    }

    override suspend fun customFetchData() = when (type) {
        TabType.FOLLOW, TabType.HOT, TabType.COOLPIC ->
            networkRepo.getDataList(dataListUrl, dataListTitle, null, lastItem, page)

        TabType.FEED -> networkRepo.getHomeFeed(page, firstLaunch, installTime, null, null)

        else -> throw IllegalArgumentException("invalid type: ${type.name}")
    }

    private fun setInstallTime() {
        viewModelScope.launch {
            userPreferencesRepository.setInstallTime(installTime)
        }
    }

}