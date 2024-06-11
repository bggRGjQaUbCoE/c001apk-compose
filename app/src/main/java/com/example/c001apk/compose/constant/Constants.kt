package com.example.c001apk.compose.constant

/**
 * Created by bggRGjQaUbCoE on 2024/6/3
 */
object Constants {
    const val REQUEST_WITH = "XMLHttpRequest"
    const val LOCALE = "zh-CN"
    const val APP_ID = "com.coolapk.market"
    var DARK_MODE = "0"
    const val CHANNEL = "coolapk"
    const val MODE = "universal"
    const val APP_LABEL = "token://com.coolapk.market/dcf01e569c1e3db93a3d0fcf191a622c"
    const val VERSION_NAME = "13.4.1"
    const val API_VERSION = "13"
    const val VERSION_CODE = "2312121"
    //val USER_AGENT =
    //    "Dalvik/2.1.0 (Linux; U; Android ${PrefManager.androidVersion}; ${PrefManager.model} ${PrefManager.buildNumber}) (#Build; ${PrefManager.brand}; ${PrefManager.model}; ${PrefManager.buildNumber}; ${PrefManager.androidVersion}) +CoolMarket/${PrefManager.versionName}-${PrefManager.versionCode}-$MODE"

    const val LOADING_FAILED = "FAILED"
    const val EMPTY_STRING = ""

    const val PREFIX_COOLMARKET = "coolmarket://"
    const val PREFIX_HTTP = "http"
    const val PREFIX_APP = "/apk/"
    const val PREFIX_GAME = "/game/"
    const val PREFIX_FEED = "/feed/"
    const val PREFIX_PRODUCT = "/product/"
    const val PREFIX_TOPIC = "/t/"
    const val PREFIX_USER = "/u/"
    const val PREFIX_CAROUSEL = "/page?url="
    const val PREFIX_CAROUSEL1 = "#/feed/"
    const val PREFIX_CAROUSEL2 = "#/topic/"
    const val SUFFIX_THUMBNAIL = ".s.jpg"
    const val UTF8 = "UTF-8"
    const val URL_LOGIN = "https://account.coolapk.com/auth/login?type=mobile"
    const val WEB_LOGIN_FAILED = "网页登录失败"

    val entityTypeList =
        listOf("feed", "apk", "product", "user", "topic", "notification", "productBrand")
    val entityTemplateList =
        listOf(
            "imageCarouselCard_1",
            "iconLinkGridCard",
            "iconMiniScrollCard",
            "iconMiniGridCard",
            "imageSquareScrollCard",
            "titleCard",
            "iconScrollCard",
            "imageTextScrollCard",
            "iconTabLinkGridCard",
            "verticalColumnsFullPageCard",
        )
}