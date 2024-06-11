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
import com.example.c001apk.compose.constant.Constants
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.settings.BasicListItem
import com.example.c001apk.compose.util.PrefManager
import com.example.c001apk.compose.util.TokenDeviceUtils.getDeviceCode

/**
 * Created by bggRGjQaUbCoE on 2024/6/1
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParamsScreen(
    onBackClick: () -> Unit
) {

    val rememberScrollState = rememberScrollState()

    var versionName by remember {
        mutableStateOf(PrefManager.versionName)
    }
    var apiVersion by remember {
        mutableStateOf(PrefManager.apiVersion)
    }
    var versionCode by remember {
        mutableStateOf(PrefManager.versionCode)
    }
    var manufacturer by remember {
        mutableStateOf(PrefManager.manufacturer)
    }
    var brand by remember {
        mutableStateOf(PrefManager.brand)
    }
    var model by remember {
        mutableStateOf(PrefManager.model)
    }
    var buildNumber by remember {
        mutableStateOf(PrefManager.buildNumber)
    }
    var sdkInt by remember {
        mutableStateOf(PrefManager.sdkInt)
    }
    var androidVersion by remember {
        mutableStateOf(PrefManager.androidVersion)
    }
    var userAgent by remember {
        mutableStateOf(PrefManager.userAgent)
    }
    var xAppDevice by remember {
        mutableStateOf(PrefManager.xAppDevice)
    }
    /*val xAppToken by remember {
        mutableStateOf(PrefManager.xAppToken)
    }*/

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
                data = versionName.ifEmpty { null }
            ) {
                PrefManager.versionName = it
                versionName = it
                userAgent = updateUserAgent()
            }

            ParamsItem(
                title = "Api Version",
                data = apiVersion.ifEmpty { null }
            ) {
                PrefManager.apiVersion = it
                apiVersion = it
            }

            ParamsItem(
                title = "Version Code",
                data = versionCode.ifEmpty { null }
            ) {
                PrefManager.versionCode = it
                versionCode = it
                userAgent = updateUserAgent()
            }

            ParamsItem(
                title = "Manufacturer",
                data = manufacturer.ifEmpty { null }
            ) {
                PrefManager.manufacturer = it
                manufacturer = it
            }

            ParamsItem(
                title = "Brand",
                data = brand.ifEmpty { null }
            ) {
                PrefManager.brand = it
                brand = it
                userAgent = updateUserAgent()
            }

            ParamsItem(
                title = "Model",
                data = model.ifEmpty { null }
            ) {
                PrefManager.model = it
                model = it
                userAgent = updateUserAgent()
            }

            ParamsItem(
                title = "BuildNumber",
                data = buildNumber.ifEmpty { null }
            ) {
                PrefManager.buildNumber = it
                buildNumber = it
                userAgent = updateUserAgent()
            }

            ParamsItem(
                title = "SDK INT",
                data = sdkInt.ifEmpty { null }
            ) {
                PrefManager.sdkInt = it
                sdkInt = it
            }

            ParamsItem(
                title = "Android Version",
                data = androidVersion.ifEmpty { null }
            ) {
                PrefManager.androidVersion = it
                androidVersion = it
                userAgent = updateUserAgent()
            }

            ParamsItem(
                title = "User Agent",
                data = userAgent.ifEmpty { null }
            )/* {
                PrefManager.userAgent = it
                userAgent = it
            }*/

            ParamsItem(
                title = "X-App-Device",
                data = xAppDevice.ifEmpty { null }
            )

            /*ParamsItem(
                title = "X-App-Token",
                data = xAppToken.ifEmpty { null }
            ) {
            }*/

            BasicListItem(
                headlineText = "Regenerate Params"
            ) {
                PrefManager.xAppDevice = getDeviceCode(true)
                manufacturer = PrefManager.manufacturer
                brand = PrefManager.brand
                model = PrefManager.model
                buildNumber = PrefManager.buildNumber
                sdkInt = PrefManager.sdkInt
                androidVersion = PrefManager.androidVersion
                userAgent = PrefManager.userAgent
                xAppDevice = PrefManager.xAppDevice
            }

        }
    }



}

fun updateUserAgent(): String {
    val userAgent =
        "Dalvik/2.1.0 (Linux; U; Android ${PrefManager.androidVersion}; ${PrefManager.model} ${PrefManager.buildNumber}) (#Build; ${PrefManager.brand}; ${PrefManager.model}; ${PrefManager.buildNumber}; ${PrefManager.androidVersion}) +CoolMarket/${PrefManager.versionName}-${PrefManager.versionCode}-${Constants.MODE}"
    PrefManager.userAgent = userAgent
    return userAgent
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