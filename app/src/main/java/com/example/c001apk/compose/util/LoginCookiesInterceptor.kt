package com.example.c001apk.compose.util

import com.example.c001apk.compose.constant.Constants.APP_ID
import com.example.c001apk.compose.constant.Constants.REQUEST_WITH
import com.example.c001apk.compose.util.CookieUtil.SESSID
import com.example.c001apk.compose.util.CookieUtil.isGetCaptcha
import com.example.c001apk.compose.util.CookieUtil.isGetLoginParam
import com.example.c001apk.compose.util.CookieUtil.isGetSmsLoginParam
import com.example.c001apk.compose.util.CookieUtil.isGetSmsToken
import com.example.c001apk.compose.util.CookieUtil.isPreGetLoginParam
import com.example.c001apk.compose.util.CookieUtil.isTryLogin
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Created by bggRGjQaUbCoE on 2024/6/3
 */
object LoginCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        builder.apply {
            if (isGetLoginParam) {
                isGetLoginParam = false

                addHeader(
                    "sec-ch-ua",
                    """"Android WebView";v="117", "Not;A=Brand";v="8", "Chromium";v="117""""
                )
                addHeader("sec-ch-ua-mobile", "?1")
                addHeader("sec-ch-ua-platform", "Android")
                addHeader("Upgrade-Insecure-Requests", "1")
                addHeader("User-Agent", CookieUtil.userAgent)
                addHeader(
                    "Accept",
                    """text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"""
                )
                addHeader("User-Agent", CookieUtil.userAgent)
                addHeader("X-Requested-With", APP_ID)
                addHeader("Cookie", SESSID)
            }
            if (isPreGetLoginParam) {
                isPreGetLoginParam = false

                addHeader(
                    "sec-ch-ua",
                    """"Android WebView";v="117", "Not;A=Brand";v="8", "Chromium";v="117""""
                )
                addHeader("sec-ch-ua-mobile", "?1")
                addHeader("sec-ch-ua-platform", "Android")
                addHeader("Upgrade-Insecure-Requests", "1")
                addHeader("User-Agent", CookieUtil.userAgent)
                addHeader(
                    "Accept",
                    """text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"""
                )
                addHeader("X-Requested-With", APP_ID)
                /*addHeader("Sec-Fetch-Site", "none")
                addHeader("Sec-Fetch-Mode", "navigate")
                addHeader("Sec-Fetch-User", "?1")
                addHeader("Sec-Fetch-Dest", "document")
                addHeader("Accept-Encoding", "gzip, deflate, br")
                addHeader("Accept-Language", "zh-CM,zh;q=0.9,en-US;q=0.8,en;q=0.7")*/
            } else if (isTryLogin) {
                isTryLogin = false

                addHeader("User-Agent", CookieUtil.userAgent)
                addHeader("Cookie", "$SESSID; forward=https://www.coolapk.com")
                addHeader("X-Requested-With", REQUEST_WITH)
                addHeader("Content-Type", "application/x-www-form-urlencoded")
            } else if (isGetCaptcha) {
                isGetCaptcha = false

                addHeader("User-Agent", CookieUtil.userAgent)
                addHeader(
                    "sec-ch-ua",
                    """"Android WebView";v="117", "Not;A=Brand";v="8", "Chromium";v="117""""
                )
                addHeader("sec-ch-ua-mobile", "?1")
                addHeader("User-Agent", CookieUtil.userAgent)
                addHeader("sec-ch-ua-platform", "Android")
                addHeader(
                    "Accept",
                    """image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8"""
                )
                addHeader("X-Requested-With", APP_ID)
                addHeader("Sec-Fetch-Site", "same-origin")
                addHeader("Sec-Fetch-Mode", "no-cors")
                addHeader("Sec-Fetch-Dest", "image")
                addHeader("Referer", "https://account.coolapk.com/auth/loginByCoolapk")
                addHeader("Cookie", "$SESSID; forward=https://www.coolapk.com")
            } else if (isGetSmsToken) {
                isGetSmsToken = false
                addHeader(
                    "sec-ch-ua",
                    """"Android WebView";v="117", "Not;A=Brand";v="8", "Chromium";v="117""""
                )
                addHeader("Content-Type", "application/x-www-form-urlencoded")
                addHeader("X-Requested-With", REQUEST_WITH)
                addHeader("sec-ch-ua-mobile", "?1")
                addHeader("User-Agent", CookieUtil.userAgent)
                addHeader("sec-ch-ua-platform", "Android")
                addHeader("Accept", "*/*")
                addHeader("Origin", "https://account.coolapk.com")
                addHeader("Sec-Fetch-Site", "same-origin")
                addHeader("Sec-Fetch-Mode", "cors")
                addHeader("Sec-Fetch-Dest", "empty")
                addHeader("Referer", "https://account.coolapk.com/auth/login?type=mobile")
                addHeader("Accept-Encoding", "gzip, deflate, br")
                addHeader("Accept-Language", "zh-CM,zh;q=0.9,en-US;q=0.8,en;q=0.7")
                addHeader("Cookie", "$SESSID; forward=https://www.coolapk.com")
            } else if (isGetSmsLoginParam) {
                isGetSmsLoginParam = false
                addHeader(
                    "sec-ch-ua",
                    """"Android WebView";v="117", "Not;A=Brand";v="8", "Chromium";v="117""""
                )
                addHeader("sec-ch-ua-mobile", "?1")
                addHeader("sec-ch-ua-platform", "Android")
                addHeader("Upgrade=Insecure-Requests", "1")
                addHeader("User-Agent", CookieUtil.userAgent)
                addHeader(
                    "Accept",
                    """text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"""
                )
                addHeader("X-Requested-With", APP_ID)
                addHeader("Sec-Fetch-Site", "none")
                addHeader("Sec-Fetch-Mode", "navigate")
                addHeader("Sec-Fetch-User", "?1")
                addHeader("Sec-Fetch-Dest", "document")
                addHeader("Accept-Encoding", "gzip, deflate, br")
                addHeader("Accept-Language", "zh-CM,zh;q=0.9,en-US;q=0.8,en;q=0.7")
            }

        }
        return chain.proceed(builder.build())
    }
}