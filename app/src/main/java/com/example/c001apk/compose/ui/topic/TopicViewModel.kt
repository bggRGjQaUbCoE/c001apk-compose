package com.example.c001apk.compose.ui.topic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.logic.state.LoadingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/9
 */
@HiltViewModel(assistedFactory = TopicViewModel.ViewModelFactory::class)
class TopicViewModel @AssistedInject constructor(
    @Assisted("url") val url: String,
    @Assisted("tag") val tag: String?,
    @Assisted("id") val id: String?,
    private val networkRepo: NetworkRepo
) : ViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            @Assisted("url") url: String,
            @Assisted("tag") tag: String?,
            @Assisted("id") id: String?,
        ): TopicViewModel
    }

    lateinit var title: String

    var topicState by mutableStateOf<LoadingState<HomeFeedResponse.Data>>(LoadingState.Loading)
        private set

    init {
        fetchTopicLayout()
    }

    private fun fetchTopicLayout() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getTopicLayout(url, tag, id)
                .collect { state ->
                    topicState = state
                }
        }
    }

    fun refresh() {
        topicState = LoadingState.Loading
        fetchTopicLayout()
    }

}