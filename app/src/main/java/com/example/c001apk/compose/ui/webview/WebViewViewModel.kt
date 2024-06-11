package com.example.c001apk.compose.ui.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.logic.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by bggRGjQaUbCoE on 2024/6/11
 */
@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    fun setIsLogin(uid:String, username:String, token:String) {
        viewModelScope.launch {
            userPreferencesRepository.apply {
                setUid(uid)
                setUsername(username)
                setToken(token)
                setIsLogin(true)
            }
        }
    }

}