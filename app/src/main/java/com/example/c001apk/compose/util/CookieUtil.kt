package com.example.c001apk.compose.util

import com.example.c001apk.compose.constant.Constants.API_VERSION
import com.example.c001apk.compose.constant.Constants.VERSION_CODE
import com.example.c001apk.compose.constant.Constants.VERSION_NAME

/**
 * Created by bggRGjQaUbCoE on 2024/6/3
 */
object CookieUtil {

    var SESSID = ""
    var isPreGetLoginParam = false
    var isGetLoginParam = false
    var isTryLogin = false
    var isGetCaptcha = false
    var isGetSmsLoginParam = false
    var isGetSmsToken = false

    var atme: Int? = null
    var atcommentme: Int? = null
    var feedlike: Int? = null
    var contacts_follow: Int? = null
    var message: Int? = null
    var notification: Int = 0

    var isLogin = false
    var szlmId = ""
    var xAppDevice = ""
    var uid = ""
    var username = ""
    var token = ""
    var userAgent = ""
    var sdkInt = ""
    var versionName = VERSION_NAME
    var versionCode = VERSION_CODE
    var apiVersion = API_VERSION
    var imageQuality = 0
    var showEmoji = true
    var imageFilter = true
    var isDarkMode = false
    var showSquare = true
    var openInBrowser = false
    var recordHistory = true

}