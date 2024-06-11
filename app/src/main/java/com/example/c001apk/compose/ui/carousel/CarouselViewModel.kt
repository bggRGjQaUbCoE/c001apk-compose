package com.example.c001apk.compose.ui.carousel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.constant.Constants.entityTemplateList
import com.example.c001apk.compose.constant.Constants.entityTypeList
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/11
 */
@HiltViewModel(assistedFactory = CarouselViewModel.ViewModelFactory::class)
class CarouselViewModel @AssistedInject constructor(
    @Assisted val isInit: Boolean,
    @Assisted("url") val url: String,
    @Assisted("title") val title: String,
    private val networkRepo: NetworkRepo
) : BaseViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            isInit: Boolean,
            @Assisted("url") url: String,
            @Assisted("title") title: String
        ): CarouselViewModel
    }

    init {
        if (isInit)
            preFetchData()
        else
            fetchData()
    }

    var pageTitle by mutableStateOf("")

    private fun preFetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getDataList(url, title, null, lastItem, page)
                .collect { state ->
                    loadingState = if (state is LoadingState.Success) {
                        page++
                        pageTitle = state.response.lastOrNull()?.extraDataArr?.pageTitle ?: title
                        val response = state.response.filter {
                            it.entityType in entityTypeList || it.entityTemplate in entityTemplateList
                        } // TODO
                        firstItem = response.firstOrNull()?.id
                        lastItem = response.lastOrNull()?.id
                        LoadingState.Success(response)
                    } else state
                }
        }
    }

    override suspend fun customFetchData() =
        networkRepo.getDataList(url, title, null, lastItem, page)
}