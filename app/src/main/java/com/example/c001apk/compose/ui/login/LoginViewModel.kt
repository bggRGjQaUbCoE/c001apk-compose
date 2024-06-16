package com.example.c001apk.compose.ui.login

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.logic.model.LoginResponse
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.logic.repository.UserPreferencesRepository
import com.example.c001apk.compose.util.CookieUtil.SESSID
import com.example.c001apk.compose.util.CookieUtil.isGetCaptcha
import com.example.c001apk.compose.util.CookieUtil.isGetLoginParam
import com.example.c001apk.compose.util.CookieUtil.isPreGetLoginParam
import com.example.c001apk.compose.util.CookieUtil.isTryLogin
import com.example.c001apk.compose.util.createRequestHash
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import javax.inject.Inject

/**
 * Created by bggRGjQaUbCoE on 2024/6/10
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val networkRepo: NetworkRepo,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    lateinit var requestHash: String
    var captchaImg by mutableStateOf<Bitmap?>(null)
        private set
    var toastText by mutableStateOf<String?>(null)
        private set

    init {
        onPreGetLoginParam()
    }

    private fun onPreGetLoginParam() {
        isPreGetLoginParam = true
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.preGetLoginParam()
                .collect { result ->
                    val response = result.getOrNull()
                    val body = response?.body()?.string()

                    body?.let {
                        requestHash = Jsoup.parse(it).createRequestHash()
                    }
                    response?.apply {
                        try {
                            val session = response.headers().values("Set-Cookie")[0]
                            SESSID = session.substring(0, session.indexOf(";"))
                        } catch (e: Exception) {
                            e.printStackTrace()
                            toastText = "无法获取Cookie"
                            return@collect
                        }
                        onGetLoginParam()
                    }
                }
        }
    }

    private fun onGetLoginParam() {
        isGetLoginParam = true
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getLoginParam()
                .collect { result ->
                    val response = result.getOrNull()
                    val body = response?.body()?.string()
                    body?.let {
                        requestHash = Jsoup.parse(it).createRequestHash()
                    }
                    response?.apply {
                        try {
                            val session = response.headers().values("Set-Cookie")[0]
                            SESSID = session.substring(0, session.indexOf(";"))
                        } catch (e: Exception) {
                            toastText = "无法获取Cookie"
                            e.printStackTrace()
                        }
                    }
                }
        }
    }

    fun onGetCaptcha() {
        isGetCaptcha = true
        val timeStamp = System.currentTimeMillis().toString()
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getCaptcha("/auth/showCaptchaImage?$timeStamp")
                .collect { result ->
                    val response = result.getOrNull()
                    response?.let {
                        val responseBody = response.body()
                        captchaImg = BitmapFactory.decodeStream(responseBody?.byteStream())
                    }
                }
        }
    }

    var loginData = HashMap<String, String?>()
    fun onLogin() {
        isTryLogin = true
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.tryLogin(loginData)
                .collect { result ->
                    val response = result.getOrNull()
                    response?.body()?.let {
                        val login: LoginResponse = Gson().fromJson(
                            response.body()?.string(),
                            LoginResponse::class.java
                        )
                        if (login.status == 1) {
                            val cookies = response.headers().values("Set-Cookie")
                            val uid =
                                cookies[cookies.size - 3].substring(
                                    4,
                                    cookies[cookies.size - 3].indexOf(";")
                                )
                            val username =
                                cookies[cookies.size - 2].substring(
                                    9,
                                    cookies[cookies.size - 2].indexOf(";")
                                )
                            val token =
                                cookies[cookies.size - 1].substring(
                                    6,
                                    cookies[cookies.size - 1].indexOf(";")
                                )

                            userPreferencesRepository.apply {
                                setIsLogin(true)
                                setUid(uid)
                                setUsername(username)
                                setToken(token)
                            }
                        } else {
                            login.message?.let {
                                toastText = login.message
                                when (login.message) {
                                    "图形验证码不能为空", "图形验证码错误" -> onGetCaptcha()

                                    "密码错误" -> if (captchaImg != null) onGetCaptcha()
                                }
                            }
                        }
                    }
                }
        }
    }

    fun reset() {
        toastText = null
    }

}