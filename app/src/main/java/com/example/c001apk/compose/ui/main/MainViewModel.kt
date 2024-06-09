package com.example.c001apk.compose.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.constant.Constants.UTF8
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.util.CookieUtil
import com.example.c001apk.compose.util.PrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import javax.inject.Inject

/**
 * Created by bggRGjQaUbCoE on 2024/6/3
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkRepo: NetworkRepo
) : ViewModel() {

    init {
        getCheckLoginInfo()
    }

    private fun getCheckLoginInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.checkLoginInfo()
                .collect { result ->
                    val response = result.getOrNull()
                    response?.let {
                        response.body()?.let {
                            if (response.body()?.data?.token != null) {
                                response.body()?.data?.let { login ->
                                    CookieUtil.badge = login.notifyCount.badge
                                    CookieUtil.atme = login.notifyCount.atme
                                    CookieUtil.atcommentme = login.notifyCount.atcommentme
                                    CookieUtil.feedlike = login.notifyCount.feedlike
                                    CookieUtil.contacts_follow = login.notifyCount.contactsFollow
                                    CookieUtil.message = login.notifyCount.message
                                    PrefManager.isLogin = true
                                    PrefManager.uid = login.uid
                                    PrefManager.username =
                                        withContext(Dispatchers.IO) {
                                            URLEncoder.encode(login.username, UTF8)
                                        }
                                    PrefManager.token = login.token
                                    PrefManager.userAvatar = login.userAvatar
                                }
                            } else if (response.body()?.message == "登录信息有误") {
                                PrefManager.isLogin = false
                                PrefManager.uid = EMPTY_STRING
                                PrefManager.username =EMPTY_STRING
                                PrefManager.token = EMPTY_STRING
                                PrefManager.userAvatar = EMPTY_STRING
                            }

                            try {
                                val headers = response.headers()
                                val cookies = headers.values("Set-Cookie")
                                val session = cookies[0]
                                val sessionID = session.substring(0, session.indexOf(";"))
                                CookieUtil.SESSID = sessionID
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            // if (CookieUtil.badge != 0)
                            //  setBadge.postValue(Event(true))

                        }
                    }
                }
        }
    }


}