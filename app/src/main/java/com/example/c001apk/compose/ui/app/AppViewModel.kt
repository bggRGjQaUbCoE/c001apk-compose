package com.example.c001apk.compose.ui.app

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
 * Created by bggRGjQaUbCoE on 2024/6/10
 */
@HiltViewModel(assistedFactory = AppViewModel.ViewModelFactory::class)
class AppViewModel @AssistedInject constructor(
    @Assisted val packageName: String,
    private val networkRepo: NetworkRepo
) : ViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(packageName: String): AppViewModel
    }

    lateinit var id: String
    lateinit var title: String
    lateinit var versionName: String
    lateinit var versionCode: String

    var appState by mutableStateOf<LoadingState<HomeFeedResponse.Data>>(LoadingState.Loading)
        private set

    init {
        fetchAppInfo()
    }

    private fun fetchAppInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getAppInfo(packageName)
                .collect { state ->
                    appState = state
                }
        }
    }

    fun refresh() {
        appState = LoadingState.Loading
        fetchAppInfo()
    }

    var downloadApk by mutableStateOf(false)
        private set
    lateinit var downloadUrl: String
    fun onGetDownloadLink() {
        if (::downloadUrl.isInitialized) {
            downloadApk = true
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                networkRepo.getAppDownloadLink(packageName, id, versionCode)
                    .collect { result ->
                        val link = result.getOrNull()
                        if (link != null) {
                            downloadUrl = link
                            downloadApk = true
                        } else {
                            result.exceptionOrNull()?.printStackTrace()
                        }
                    }
            }
        }
    }

    fun reset() {
        downloadApk = false
    }


}