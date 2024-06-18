package com.example.c001apk.compose.ui.chat

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
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

/**
 * Created by bggRGjQaUbCoE on 2024/6/19
 */
@HiltViewModel(assistedFactory = ChatViewModel.ViewModelFactory::class)
class ChatViewModel @AssistedInject constructor(
    @Assisted val ukey: String,
    private val networkRepo: NetworkRepo,
    private val blackListRepo: BlackListRepo,
) : BaseViewModel(networkRepo, blackListRepo) {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(ukey: String): ChatViewModel
    }

    init {
        fetchData()
    }

    override suspend fun customFetchData() =
        networkRepo.messageOperation("/v6/message/chat", ukey, null, page, firstItem, lastItem)

    override fun onBlockUser(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            blackListRepo.saveUid(uid)
        }
    }

    override fun handleResponse(response: List<HomeFeedResponse.Data>): List<HomeFeedResponse.Data> {
        return response.reversed()
    }

    fun onGetImageUrl(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getImageUrl(id)
                .collect { result ->
                    result.getOrNull()?.let { imageUrl ->
                        var response = (loadingState as LoadingState.Success).response
                        response = response.map { item ->
                            if (item.id == id)
                                item.copy(messagePic = imageUrl)
                            else item
                        }
                        loadingState = LoadingState.Success(response)
                    }
                }
        }
    }

    fun onDeleteMsg() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.deleteMessage("/v6/message/delete", ukey, deleteId)
                .collect { result ->
                    result.getOrNull()?.let { data ->
                        if (data.message != null) {
                            toastText = data.message
                        } else if (data.data?.count != null) {
                            var response = (loadingState as LoadingState.Success).response
                            response = response.filterNot { it.id == deleteId }
                            loadingState = LoadingState.Success(response)
                            toastText = data.data.count
                        }
                    }
                }
        }
    }

    lateinit var deleteId: String
    lateinit var message: String
    lateinit var pic: String
    var scroll by mutableStateOf(false)
        private set

    fun reset() {
        scroll = false
    }

    fun onSendMessage(uid: String, text: String, url: String) {
        val message = MultipartBody.Part.createFormData("message", text)
        val pic = MultipartBody.Part.createFormData("message_pic", url)
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.sendMessage(uid, message, pic)
                .collect { result ->
                    result.getOrNull()?.let { data ->
                        if (data.message != null) {
                            toastText = data.message
                        } else if (!data.data.isNullOrEmpty()) {
                            val response =
                                (loadingState as LoadingState.Success).response.toMutableList()
                                    .also {
                                        it.addAll(0, data.data)
                                    }
                            loadingState = LoadingState.Success(response)
                            scroll = true
                        }
                    }
                }
        }
    }

}