package com.example.c001apk.compose.ui.notification

import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.logic.repository.BlackListRepo
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.base.BaseViewModel
import com.example.c001apk.compose.ui.ffflist.FFFContentViewModel.ActionType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/13
 */
@HiltViewModel(assistedFactory = NoticeViewModel.ViewModelFactory::class)
class NoticeViewModel @AssistedInject constructor(
    @Assisted val url: String,
    private val networkRepo: NetworkRepo,
    blackListRepo: BlackListRepo,
) : BaseViewModel(networkRepo, blackListRepo) {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(url: String): NoticeViewModel
    }

    init {
        fetchData()
    }

    override suspend fun customFetchData() = networkRepo.getMessage(url, page, lastItem)

    var isTop = 0
    lateinit var ukey: String

    fun onHandleMessage(actionType: ActionType) {
        val url = when (actionType) {
            ActionType.TOP -> "/v6/message/${if (isTop == 1) "removeTop" else "addTop"}"
            ActionType.DELETE -> "/v6/message/deleteChat"
            ActionType.DELETE_ALL -> EMPTY_STRING
        }
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.deleteMessage(url, ukey, null)
                .collect { result ->
                    result.getOrNull()?.let { data ->
                        if (!data.message.isNullOrEmpty()) {
                            toastText = data.message
                        } else if (data.data?.count?.contains("成功") == true) {
                            var response = (loadingState as LoadingState.Success).response
                            response = when (actionType) {
                                ActionType.TOP -> {
                                    if (isTop == 0) {
                                        val actionItem = response.find { it.ukey == ukey }
                                        response.toMutableList().also {
                                            it.remove(actionItem)
                                            actionItem?.let { item ->
                                                item.isTop = 1
                                                it.add(0, item)
                                            }
                                        }
                                    } else {
                                        response.map {
                                            if (it.ukey == ukey) it.copy(isTop = 0)
                                            else it
                                        }
                                    }
                                }

                                ActionType.DELETE -> response.filterNot { it.ukey == ukey }
                                ActionType.DELETE_ALL -> response
                            }
                            loadingState = LoadingState.Success(response)
                            toastText = data.data.count
                        }
                    }
                }
        }
    }

    fun resetUnRead(ukey: String) {
        viewModelScope.launch {
            var response = (loadingState as LoadingState.Success).response
            response = response.map { item ->
                if (item.ukey == ukey)
                    item.copy(unreadNum = 0)
                else
                    item
            }
            loadingState = LoadingState.Success(response)
        }
    }

}