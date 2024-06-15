package com.example.c001apk.compose.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.constant.Constants
import com.example.c001apk.compose.logic.repository.UserPreferencesRepository
import com.example.c001apk.compose.util.CookieUtil.szlmId
import com.example.c001apk.compose.util.CookieUtil.versionCode
import com.example.c001apk.compose.util.CookieUtil.versionName
import com.example.c001apk.compose.util.TokenDeviceUtils.encode
import com.example.c001apk.compose.util.TokenDeviceUtils.randHexString
import com.example.c001apk.compose.util.Utils.randomAndroidVersionRelease
import com.example.c001apk.compose.util.Utils.randomBrand
import com.example.c001apk.compose.util.Utils.randomDeviceModel
import com.example.c001apk.compose.util.Utils.randomMacAddress
import com.example.c001apk.compose.util.Utils.randomManufacturer
import com.example.c001apk.compose.util.Utils.randomSdkInt
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/15
 */
open class PrefsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    fun regenerateParams() {
        viewModelScope.launch {
            val manufacturer = randomManufacturer()
            val brand = randomBrand()
            val model = randomDeviceModel()
            val buildNumber = randHexString(16)
            val sdkInt = randomSdkInt()
            val androidVersion = randomAndroidVersionRelease()
            val mac = randomMacAddress()
            val userAgent =
                "Dalvik/2.1.0 (Linux; U; Android $androidVersion; $model $buildNumber) (#Build; $brand; $model; $buildNumber; $androidVersion) +CoolMarket/$versionName-$versionCode-${Constants.MODE}"
            val xAppDevice =
                encode("$szlmId; ; ; $mac; $manufacturer; $brand; $model; $buildNumber; null")

            userPreferencesRepository.apply {
                setManufacturer(manufacturer)
                setBrand(brand)
                setModel(model)
                setBuildNumber(buildNumber)
                setSdkInt(sdkInt)
                setAndroidVersion(androidVersion)
                setUserAgent(userAgent)
                setXAppDevice(xAppDevice)
            }

        }
    }
}