package com.example.c001apk.compose.ui.blacklist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.logic.model.StringEntity
import com.example.c001apk.compose.logic.repository.BlackListRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/16
 */
@HiltViewModel(assistedFactory = BlackListViewModel.ViewModelFactory::class)
class BlackListViewModel @AssistedInject constructor(
    @Assisted val type: BlackListType,
    private val blackListRepo: BlackListRepo
) : ViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(type: BlackListType): BlackListViewModel
    }

    val blackList: Flow<List<StringEntity>> = when (type) {
        BlackListType.USER -> blackListRepo.loadAllUserListFlow()
        BlackListType.TOPIC -> blackListRepo.loadAllTopicListFlow()
    }

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                BlackListType.USER -> blackListRepo.deleteAllUser()
                BlackListType.TOPIC -> blackListRepo.deleteAllTopic()
            }
        }
    }

    fun delete(data: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                BlackListType.USER -> blackListRepo.deleteUid(data)
                BlackListType.TOPIC -> blackListRepo.deleteTopic(data)
            }
        }
    }

    var toastText by mutableStateOf<String?>(null)
    fun save(data: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                BlackListType.USER -> {
                    if (blackListRepo.checkUid(data))
                        toast()
                    else
                        blackListRepo.insertUid(data)
                }

                BlackListType.TOPIC -> {
                    if (blackListRepo.checkTopic(data))
                        toast()
                    else
                        blackListRepo.insertTopic(data)
                }
            }
        }
    }

    private fun toast() {
        toastText = "已存在"
    }

    fun reset() {
        toastText = null
    }

    fun insertList(list: List<StringEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                BlackListType.USER -> blackListRepo.insertUidList(list)
                BlackListType.TOPIC -> blackListRepo.insertTopicList(list)
            }
        }
    }

}