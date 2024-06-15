package com.example.c001apk.compose.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.constant.Constants
import com.example.c001apk.compose.logic.providable.LocalUserPreferences
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.settings.BasicListItem
import com.example.c001apk.compose.util.TokenDeviceUtils.encode
import com.example.c001apk.compose.util.Utils.randomMacAddress

/**
 * Created by bggRGjQaUbCoE on 2024/6/1
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParamsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {

    val rememberScrollState = rememberScrollState()
    val prefs = LocalUserPreferences.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton { onBackClick() }
                },
                title = { Text(text = "Params") },
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState)
        ) {

            ParamsItem(
                title = "Version Name",
                data = prefs.versionName.ifEmpty { null }
            ) {
                viewModel.setVersionName(it)
                viewModel.setUserAgent(
                    "Dalvik/2.1.0 (Linux; U; Android ${prefs.androidVersion}; ${prefs.model} ${prefs.buildNumber}) (#Build; ${prefs.brand}; ${prefs.model}; ${prefs.buildNumber}; ${prefs.androidVersion}) +CoolMarket/$it-${prefs.versionCode}-${Constants.MODE}"
                )
            }

            ParamsItem(
                title = "Api Version",
                data = prefs.apiVersion.ifEmpty { null }
            ) {
                viewModel.setApiVersion(it)
            }

            ParamsItem(
                title = "Version Code",
                data = prefs.versionCode.ifEmpty { null }
            ) {
                viewModel.setVersionCode(it)
                viewModel.setUserAgent(
                    "Dalvik/2.1.0 (Linux; U; Android ${prefs.androidVersion}; ${prefs.model} ${prefs.buildNumber}) (#Build; ${prefs.brand}; ${prefs.model}; ${prefs.buildNumber}; ${prefs.androidVersion}) +CoolMarket/${prefs.versionName}-$it-${Constants.MODE}"
                )
            }

            ParamsItem(
                title = "Manufacturer",
                data = prefs.manufacturer.ifEmpty { null }
            ) {
                viewModel.setManufacturer(it)
                viewModel.setXAppDevice(
                    encode("${prefs.szlmId}; ; ; ${randomMacAddress()}; $it; ${prefs.brand}; ${prefs.model}; ${prefs.buildNumber}; null")
                )
            }

            ParamsItem(
                title = "Brand",
                data = prefs.brand.ifEmpty { null }
            ) {
                viewModel.setBrand(it)
                viewModel.setXAppDevice(
                    encode("${prefs.szlmId}; ; ; ${randomMacAddress()}; ${prefs.manufacturer}; $it; ${prefs.model}; ${prefs.buildNumber}; null")
                )
                viewModel.setUserAgent(
                    "Dalvik/2.1.0 (Linux; U; Android ${prefs.androidVersion}; ${prefs.model} ${prefs.buildNumber}) (#Build; $it; ${prefs.model}; ${prefs.buildNumber}; ${prefs.androidVersion}) +CoolMarket/${prefs.versionName}-${prefs.versionCode}-${Constants.MODE}"
                )
            }

            ParamsItem(
                title = "Model",
                data = prefs.model.ifEmpty { null }
            ) {
                viewModel.setModel(it)
                viewModel.setXAppDevice(
                    encode("${prefs.szlmId}; ; ; ${randomMacAddress()}; ${prefs.manufacturer}; ${prefs.brand}; $it; ${prefs.buildNumber}; null")
                )
                viewModel.setUserAgent(
                    "Dalvik/2.1.0 (Linux; U; Android ${prefs.androidVersion}; $it ${prefs.buildNumber}) (#Build; ${prefs.brand}; $it; ${prefs.buildNumber}; ${prefs.androidVersion}) +CoolMarket/${prefs.versionName}-${prefs.versionCode}-${Constants.MODE}"
                )
            }

            ParamsItem(
                title = "BuildNumber",
                data = prefs.buildNumber.ifEmpty { null }
            ) {
                viewModel.setBuildNumber(it)
                viewModel.setXAppDevice(
                    encode("${prefs.szlmId}; ; ; ${randomMacAddress()}; ${prefs.manufacturer}; ${prefs.brand}; ${prefs.model}; $it; null")
                )
                viewModel.setUserAgent(
                    "Dalvik/2.1.0 (Linux; U; Android ${prefs.androidVersion}; ${prefs.model} ${prefs.buildNumber}) (#Build; ${prefs.brand}; ${prefs.model}; $it; ${prefs.androidVersion}) +CoolMarket/${prefs.versionName}-${prefs.versionCode}-${Constants.MODE}"
                )
            }

            ParamsItem(
                title = "SDK INT",
                data = prefs.sdkInt.ifEmpty { null }
            ) {
                viewModel.setSdkInt(it)
            }

            ParamsItem(
                title = "Android Version",
                data = prefs.androidVersion.ifEmpty { null }
            ) {
                viewModel.setAndroidVersion(it)
                viewModel.setUserAgent(
                    "Dalvik/2.1.0 (Linux; U; Android $it; ${prefs.model} ${prefs.buildNumber}) (#Build; ${prefs.brand}; ${prefs.model}; ${prefs.buildNumber}; $it) +CoolMarket/${prefs.versionName}-${prefs.versionCode}-${Constants.MODE}"
                )
            }

            ParamsItem(
                title = "User Agent",
                data = prefs.userAgent.ifEmpty { null }
            )

            ParamsItem(
                title = "X-App-Device",
                data = prefs.xAppDevice.ifEmpty { null }
            )

            BasicListItem(
                headlineText = "Regenerate Params"
            ) {
                viewModel.regenerateParams()
            }

        }
    }

}

@Composable
fun ParamsItem(
    title: String,
    data: String?,
    setData: ((String) -> Unit)? = null
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    BasicListItem(
        headlineText = title,
        supportingText = data
    ) {
        setData?.let {
            showDialog = true
        }
    }

    when {
        showDialog -> {
            EditTextDialog(
                data = data.orEmpty(),
                title = title,
                onDismiss = {
                    showDialog = false
                },
                setData = {
                    if (setData != null) {
                        setData(it)
                    }
                }
            )
        }
    }

}