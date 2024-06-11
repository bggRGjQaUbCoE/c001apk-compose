package com.example.c001apk.compose.util

import com.example.c001apk.compose.constant.Constants.APP_ID
import com.example.c001apk.compose.constant.Constants.CHANNEL
import com.example.c001apk.compose.constant.Constants.DARK_MODE
import com.example.c001apk.compose.constant.Constants.LOCALE
import com.example.c001apk.compose.constant.Constants.MODE
import com.example.c001apk.compose.constant.Constants.REQUEST_WITH
import com.example.c001apk.compose.util.CookieUtil.SESSID
import com.example.c001apk.compose.util.TokenDeviceUtils.getLastingDeviceCode
import com.example.c001apk.compose.util.TokenDeviceUtils.getTokenV2
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Created by bggRGjQaUbCoE on 2024/6/3
 */
object AddCookiesInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val deviceCode = getLastingDeviceCode()
        val token = deviceCode.getTokenV2()
        builder.apply {
            addHeader("User-Agent", PrefManager.userAgent)
            addHeader("X-Requested-With", REQUEST_WITH)
            addHeader("X-Sdk-Int", PrefManager.sdkInt)
            addHeader("X-Sdk-Locale", LOCALE)
            addHeader("X-App-Id", APP_ID)
            addHeader("X-App-Token", token)
            addHeader("X-App-Version", PrefManager.versionName)
            addHeader("X-App-Code", PrefManager.versionCode)
            addHeader("X-Api-Version", PrefManager.apiVersion)
            addHeader("X-App-Device", deviceCode)
            addHeader("X-Dark-Mode", DARK_MODE)
            addHeader("X-App-Channel", CHANNEL)
            addHeader("X-App-Mode", MODE)
            addHeader("X-App-Supported", PrefManager.versionCode)
            addHeader("Content-Type", "application/x-www-form-urlencoded")
            if (CookieUtil.isLogin)
                addHeader(
                    "Cookie",
                    "uid=${CookieUtil.uid}; username=${CookieUtil.username}; token=${CookieUtil.token}"
                )
            else addHeader("Cookie", SESSID)
        }
        return chain.proceed(builder.build())
    }
}