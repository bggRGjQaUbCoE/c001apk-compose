package com.example.c001apk.compose.logic.model

import android.content.pm.PackageInfo

data class AppItem(
    var label: String,
    val packageInfo: PackageInfo
)