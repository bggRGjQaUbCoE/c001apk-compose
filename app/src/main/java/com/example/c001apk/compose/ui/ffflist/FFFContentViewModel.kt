package com.example.c001apk.compose.ui.ffflist

import androidx.lifecycle.viewModelScope
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

/**
 * Created by bggRGjQaUbCoE on 2024/6/12
 */
@HiltViewModel(assistedFactory = FFFContentViewModel.ViewModelFactory::class)
class FFFContentViewModel @AssistedInject constructor(
    @Assisted("url") val url: String,
    @Assisted("uid") val uid: String?,
    @Assisted("id") val id: String?,
    @Assisted val showDefault: Int?,
    private val networkRepo: NetworkRepo,
    blackListRepo: BlackListRepo,
) : BaseViewModel(networkRepo, blackListRepo) {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            @Assisted("url") url: String,
            @Assisted("uid") uid: String?,
            @Assisted("id") id: String?,
            @Assisted showDefault: Int?,
        ): FFFContentViewModel
    }

    init {
        fetchData()
    }

    override suspend fun customFetchData() =
        networkRepo.getFollowList(url, uid, id, showDefault, page, lastItem)

    enum class ActionType {
        TOP, DELETE, DELETE_ALL
    }

    var isTop = 0
    lateinit var actionId: String
    lateinit var targetId: String
    lateinit var targetType: String

    fun onHandleRecentHistory(actionType: ActionType) {
        val url = "/v6/user/" + when (actionType) {
            ActionType.TOP -> if (isTop == 0) "addToTop" else "removeFromTop"
            ActionType.DELETE -> "delete"
            ActionType.DELETE_ALL -> "clear"
        } + "RecentHistory"
        val data = when (actionType) {
            ActionType.TOP -> if (isTop == 1) mapOf("id" to actionId)
            else mapOf(
                "targetId" to targetId,
                "targetType" to targetType,
            )

            ActionType.DELETE -> mapOf("id" to actionId)
            ActionType.DELETE_ALL -> null
        }
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.postDelete(url, data)
                .collect { result ->
                    result.getOrNull()?.let { data ->
                        if (!data.message.isNullOrEmpty()) {
                            toastText = data.message
                        } else if (data.data?.count?.contains("成功") == true) {
                            var response = (loadingState as LoadingState.Success).response
                            response = when (actionType) {
                                ActionType.TOP -> {
                                    if (isTop == 0) {
                                        val actionItem = response.find { it.targetId == targetId }
                                        response.toMutableList().also {
                                            it.remove(actionItem)
                                            actionItem?.let { item ->
                                                item.isTop = 1
                                                it.add(0, item)
                                            }
                                        }
                                    } else {
                                        response.map {
                                            if (it.targetId == targetId) it.copy(isTop = 0)
                                            else it
                                        }
                                    }
                                }

                                ActionType.DELETE -> response.filterNot { item -> item.targetId == targetId }
                                ActionType.DELETE_ALL -> emptyList()
                            }
                            loadingState = LoadingState.Success(response)
                            toastText = data.data.count
                        }
                    }
                }
        }
    }

}