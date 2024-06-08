package com.example.c001apk.compose.util

import com.example.c001apk.compose.c001Application
import com.example.c001apk.compose.constant.Constants

object PrefManager {

    private val pref = c001Application.prefs

    var showEmoji: Boolean
        get() = pref.getBoolean("showEmoji", true)
        set(value) = pref.edit().putBoolean("showEmoji", value).apply()

    var isLogin: Boolean
        get() = pref.getBoolean("isLogin", false)
        set(value) = pref.edit().putBoolean("isLogin", value).apply()

    var uid: String
        get() = pref.getString("uid", "")!!
        set(value) = pref.edit().putString("uid", value).apply()

    var username: String
        get() = pref.getString("username", "")!!
        set(value) = pref.edit().putString("username", value).apply()

    var token: String
        get() = pref.getString("token", "")!!
        set(value) = pref.edit().putString("token", value).apply()

    var userAvatar: String
        get() = pref.getString("userAvatar", "")!!
        set(value) = pref.edit().putString("userAvatar", value).apply()

    var level: String
        get() = pref.getString("level", "")!!
        set(value) = pref.edit().putString("level", value).apply()

    var experience: String
        get() = pref.getString("experience", "")!!
        set(value) = pref.edit().putString("experience", value).apply()

    var nextLevelExperience: String
        get() = pref.getString("nextLevelExperience", "")!!
        set(value) = pref.edit().putString("nextLevelExperience", value).apply()

    var xAppToken: String
        get() = pref.getString("xAppToken", "")!!
        set(value) = pref.edit().putString("xAppToken", value).apply()

    var xAppDevice: String
        get() = pref.getString("xAppDevice", "")!!
        set(value) = pref.edit().putString("xAppDevice", value).apply()

    var customToken: Boolean
        get() = pref.getBoolean("customToken", false)
        set(value) = pref.edit().putBoolean("customToken", value).apply()

    var versionName: String
        get() = pref.getString("versionName", Constants.VERSION_NAME)!!
        set(value) = pref.edit().putString("versionName", value).apply()

    var apiVersion: String
        get() = pref.getString("apiVersion", Constants.API_VERSION)!!
        set(value) = pref.edit().putString("apiVersion", value).apply()

    var versionCode: String
        get() = pref.getString("versionCode", Constants.VERSION_CODE)!!
        set(value) = pref.edit().putString("versionCode", value).apply()

    var manufacturer: String
        get() = pref.getString("manufacturer", "")!!
        set(value) = pref.edit().putString("manufacturer", value).apply()

    var brand: String
        get() = pref.getString("brand", "")!!
        set(value) = pref.edit().putString("brand", value).apply()

    var model: String
        get() = pref.getString("model", "")!!
        set(value) = pref.edit().putString("model", value).apply()

    var buildNumber: String
        get() = pref.getString("buildNumber", "")!!
        set(value) = pref.edit().putString("buildNumber", value).apply()

    var sdkInt: String
        get() = pref.getString("sdkInt", "")!!
        set(value) = pref.edit().putString("sdkInt", value).apply()

    var androidVersion: String
        get() = pref.getString("androidVersion", "")!!
        set(value) = pref.edit().putString("androidVersion", value).apply()

    var userAgent: String
        get() = pref.getString("userAgent", "")!!
        set(value) = pref.edit().putString("userAgent", value).apply()

    var szlmId: String
        get() = pref.getString("szlmId", "")!!
        set(value) = pref.edit().putString("szlmId", value).apply()

    var isRecordHistory: Boolean
        get() = pref.getBoolean("isRecordHistory", true)
        set(value) = pref.edit().putBoolean("isRecordHistory", value).apply()

    var fontScale: String
        get() = pref.getString("fontScale", "1.00")!!
        set(value) = pref.edit().putString("fontScale", value).apply()

    var isIconMiniCard: Boolean
        get() = pref.getBoolean("isIconMiniCard", true)
        set(value) = pref.edit().putBoolean("isIconMiniCard", value).apply()

    var isOpenLinkOutside: Boolean
        get() = pref.getBoolean("isOpenLinkOutside", false)
        set(value) = pref.edit().putBoolean("isOpenLinkOutside", value).apply()

    var followType: String
        get() = pref.getString("followType", "all")!!
        set(value) = pref.edit().putString("followType", value).apply()

    var imageQuality: Int
        get() = pref.getInt("imageQuality", 0)
        set(value) = pref.edit().putInt("imageQuality", value).apply()

    var isColorFilter: Boolean
        get() = pref.getBoolean("isColorFilter", true)
        set(value) = pref.edit().putBoolean("isColorFilter", value).apply()

    var isCheckUpdate: Boolean
        get() = pref.getBoolean("isCheckUpdate", true)
        set(value) = pref.edit().putBoolean("isCheckUpdate", value).apply()

    var recentIds: String
        get() = pref.getString("recentIds", "")!!
        set(value) = pref.edit().putString("recentIds", value).apply()

    var isCheckCount: Boolean
        get() = pref.getBoolean("isCheckCount", true)
        set(value) = pref.edit().putBoolean("isCheckCount", value).apply()

    var checkCountPeriod: Int
        get() = pref.getInt("checkCountPeriod", 5) // min
        set(value) = pref.edit().putInt("checkCountPeriod", value).apply()

}