package com.example.c001apk.compose.ui.settings

import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.FollowType
import com.example.c001apk.compose.ThemeMode
import com.example.c001apk.compose.logic.repository.UserPreferencesRepository
import com.example.c001apk.compose.ui.base.PrefsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by bggRGjQaUbCoE on 2024/5/31
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : PrefsViewModel(userPreferencesRepository) {

    fun setDarkTheme(value: ThemeMode) {
        viewModelScope.launch {
            userPreferencesRepository.setThemeMode(value)
        }
    }

    fun setMaterialYou(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setMaterialYou(value)
        }
    }

    fun setPureBlack(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setPureBlack(value)
        }
    }

    fun setFontScale(value: Float) {
        viewModelScope.launch {
            userPreferencesRepository.setFontScale(value)
        }
    }

    fun setContentScale(value: Float) {
        viewModelScope.launch {
            userPreferencesRepository.setContentScale(value)
        }
    }

    fun setSZLMId(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setSZLMId(value)
        }
    }

    fun setImageQuality(value: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setImageQuality(value)
        }
    }

    fun setImageFilter(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setImageFilter(value)
        }
    }

    fun setOpenInBrowser(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setOpenInBrowser(value)
        }
    }

    fun setShowSquare(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setShowSquare(value)
        }
    }

    fun setRecordHistory(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setRecordHistory(value)
        }
    }

    fun setShowEmoji(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setShowEmoji(value)
        }
    }

    fun setCheckUpdate(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setCheckUpdate(value)
        }
    }

    fun setCheckCount(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setCheckCount(value)
        }
    }

    fun setVersionName(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setVersionName(value)
        }
    }

    fun setApiVersion(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setApiVersion(value)
        }
    }

    fun setVersionCode(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setVersionCode(value)
        }
    }

    fun setManufacturer(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setManufacturer(value)
        }
    }

    fun setBrand(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setBrand(value)
        }
    }

    fun setModel(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setModel(value)
        }
    }

    fun setBuildNumber(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setBuildNumber(value)
        }
    }

    fun setSdkInt(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setSdkInt(value)
        }
    }

    fun setAndroidVersion(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setAndroidVersion(value)
        }
    }

    fun setUserAgent(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setUserAgent(value)
        }
    }

    fun setXAppDevice(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setXAppDevice(value)
        }
    }

    fun setXAppToken(value: String) {
        viewModelScope.launch {
            userPreferencesRepository.setXAppToken(value)
        }
    }

    fun setFollowType(value: FollowType) {
        viewModelScope.launch {
            userPreferencesRepository.setFollowType(value)
        }
    }

}