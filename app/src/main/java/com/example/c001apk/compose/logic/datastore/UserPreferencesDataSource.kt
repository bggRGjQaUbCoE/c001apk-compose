package com.example.c001apk.compose.logic.datastore

import androidx.datastore.core.DataStore
import com.example.c001apk.compose.FollowType
import com.example.c001apk.compose.ThemeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferencesCompat>
) {
    val data get() = userPreferences.data

    suspend fun setThemeMode(value: ThemeMode) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(themeMode = value) }
    }

    suspend fun setMaterialYou(value: Boolean) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(materialYou = value) }
    }

    suspend fun setPureBlack(value: Boolean) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(pureBlack = value) }
    }

    suspend fun setFontScale(value: Float) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(fontScale = value) }
    }

    suspend fun setContentScale(value: Float) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(contentScale = value) }
    }

    suspend fun setSZLMId(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(szlmId = value) }
    }

    suspend fun setImageQuality(value: Int) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(imageQuality = value) }
    }

    suspend fun setImageFilter(value: Boolean) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(imageFilter = value) }
    }

    suspend fun setOpenInBrowser(value: Boolean) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(openInBrowser = value) }
    }

    suspend fun setShowSquare(value: Boolean) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(showSquare = value) }
    }

    suspend fun setRecordHistory(value: Boolean) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(recordHistory = value) }
    }

    suspend fun setShowEmoji(value: Boolean) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(showEmoji = value) }
    }

    suspend fun setCheckUpdate(value: Boolean) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(checkUpdate = value) }
    }

    suspend fun setCheckCount(value: Boolean) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(checkCount = value) }
    }

    suspend fun setVersionName(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(versionName = value) }
    }

    suspend fun setApiVersion(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(apiVersion = value) }
    }

    suspend fun setVersionCode(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(versionCode = value) }
    }

    suspend fun setManufacturer(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(manufacturer = value) }
    }

    suspend fun setBrand(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(brand = value) }
    }

    suspend fun setModel(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(model = value) }
    }

    suspend fun setBuildNumber(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(buildNumber = value) }
    }

    suspend fun setSdkInt(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(sdkInt = value) }
    }

    suspend fun setAndroidVersion(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(androidVersion = value) }
    }

    suspend fun setUserAgent(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(userAgent = value) }
    }

    suspend fun setXAppDevice(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(xAppDevice = value) }
    }

    suspend fun setXAppToken(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(xAppToken = value) }
    }

    suspend fun setIsLogin(value: Boolean) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(isLogin = value) }
    }

    suspend fun setUserAvatar(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(userAvatar = value) }
    }

    suspend fun setUsername(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(username = value) }
    }

    suspend fun setLevel(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(level = value) }
    }

    suspend fun setExperience(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(experience = value) }
    }

    suspend fun setNextLevelExperience(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(nextLevelExperience = value) }
    }

    suspend fun setUid(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(uid = value) }
    }

    suspend fun setToken(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(token = value) }
    }

    suspend fun setFollowType(value: FollowType) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(followType = value) }
    }

    suspend fun setRecentIds(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(recentIds = value) }
    }

    suspend fun setCheckCountPeriod(value: Int) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(checkCountPeriod = value) }
    }

    suspend fun setInstallTime(value: String) = withContext(Dispatchers.IO) {
        userPreferences.updateData { it.copy(installTime = value) }
    }

}