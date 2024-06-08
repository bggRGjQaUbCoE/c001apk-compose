package com.example.c001apk.compose.logic.repository

import com.example.c001apk.compose.ThemeMode
import com.example.c001apk.compose.logic.datastore.UserPreferencesDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) {
    val data get() = userPreferencesDataSource.data

    suspend fun setThemeMode(value: ThemeMode) = userPreferencesDataSource.setThemeMode(value)

    suspend fun setMaterialYou(value: Boolean) = userPreferencesDataSource.setMaterialYou(value)

    suspend fun setPureBlack(value: Boolean) = userPreferencesDataSource.setPureBlack(value)

    suspend fun setFontScale(value: Float) = userPreferencesDataSource.setFontScale(value)

    suspend fun setContentScale(value: Float) = userPreferencesDataSource.setContentScale(value)

    suspend fun setSZLMId(value: String) = userPreferencesDataSource.setSZLMId(value)

    suspend fun setImageQuality(value: Int) = userPreferencesDataSource.setImageQuality(value)

    suspend fun setImageFilter(value: Boolean) = userPreferencesDataSource.setImageFilter(value)

    suspend fun setOpenInBrowser(value: Boolean) = userPreferencesDataSource.setOpenInBrowser(value)

    suspend fun setShowSquare(value: Boolean) = userPreferencesDataSource.setShowSquare(value)

    suspend fun setRecordHistory(value: Boolean) = userPreferencesDataSource.setRecordHistory(value)

    suspend fun setShowEmoji(value: Boolean) = userPreferencesDataSource.setShowEmoji(value)

    suspend fun setCheckUpdate(value: Boolean) = userPreferencesDataSource.setCheckUpdate(value)

    suspend fun setCheckCount(value: Boolean) = userPreferencesDataSource.setCheckCount(value)

    suspend fun setVersionName(value: String) = userPreferencesDataSource.setVersionName(value)

    suspend fun setApiVersion(value: String) = userPreferencesDataSource.setApiVersion(value)

    suspend fun setVersionCode(value: String) = userPreferencesDataSource.setVersionCode(value)

    suspend fun setManufacturer(value: String) = userPreferencesDataSource.setManufacturer(value)

    suspend fun setBrand(value: String) = userPreferencesDataSource.setBrand(value)

    suspend fun setModel(value: String) = userPreferencesDataSource.setModel(value)

    suspend fun setBuildNumber(value: String) = userPreferencesDataSource.setBuildNumber(value)

    suspend fun setSdkInt(value: String) = userPreferencesDataSource.setSdkInt(value)

    suspend fun setAndroidVersion(value: String) = userPreferencesDataSource.setAndroidVersion(value)

    suspend fun setUserAgent(value: String) = userPreferencesDataSource.setUserAgent(value)

    suspend fun setXAppDevice(value: String) = userPreferencesDataSource.setXAppDevice(value)

    suspend fun setXAppToken(value: String) = userPreferencesDataSource.setXAppToken(value)

}