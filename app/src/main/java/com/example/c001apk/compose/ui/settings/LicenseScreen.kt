package com.example.c001apk.compose.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.settings.BasicListItem

/**
 * Created by bggRGjQaUbCoE on 2024/6/2
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseScreen(
    onBackClick: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumTopAppBar(
                navigationIcon = {
                    BackButton(onBackClick)
                },
                title = {
                    Text(text = "Open Source License")
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(paddingValues)
        ) {
            items(licenseList) { item ->
                LicenseRow(item = item)
            }
        }
    }

}

private val licenseList = listOf(
    LicenseItem(
        "Google",
        "Jetpack Compose",
        "https://github.com/androidx/androidx",
        LicenseType.Apache2
    ),
    LicenseItem(
        "JetBrains",
        "Kotlin",
        "https://github.com/JetBrains/kotlin",
        LicenseType.Apache2
    ),
    LicenseItem(
        "Google",
        "Accompanist",
        "https://github.com/google/accompanist",
        LicenseType.Apache2
    ),
    LicenseItem(
        "Google",
        "Material Design 3",
        "https://m3.material.io/",
        LicenseType.Apache2
    ),
    LicenseItem(
        "Google",
        "Material Icons",
        "https://github.com/google/material-design-icons",
        LicenseType.Apache2
    ),
    LicenseItem(
        "square",
        "okhttp",
        "https://github.com/square/okhttp",
        LicenseType.Apache2
    ),
    LicenseItem(
        "square",
        "retrofit",
        "https://github.com/square/retrofit",
        LicenseType.Apache2
    ),
    LicenseItem(
        "mikaelzero",
        "mojito",
        "https://github.com/mikaelzero/mojito",
        LicenseType.Apache2
    ),
    LicenseItem(
        "coil-kt",
        "coil",
        "https://github.com/coil-kt/coil",
        LicenseType.Apache2
    ),
    LicenseItem(
        "wasabeef",
        "transformers",
        "https://github.com/wasabeef/transformers",
        LicenseType.Apache2
    ),
    LicenseItem(
        "jeremyh",
        "jBCrypt",
        "https://github.com/jeremyh/jBCrypt",
        LicenseType.Apache2
    ),
    LicenseItem(
        "jhy",
        "jsoup",
        "https://github.com/jhy/jsoup",
        LicenseType.MIT
    ),
)

data class LicenseItem(
    val author: String,
    val name: String,
    val url: String,
    val type: LicenseType
)

enum class LicenseType {
    Apache2,
    MIT,
    GPL3
}

private fun getLicense(type: LicenseType): String =
    when (type) {
        LicenseType.Apache2 -> "Apache Software License 2.0"
        LicenseType.MIT -> "MIT License"
        LicenseType.GPL3 -> "GNU general public license Version 3"
    }

@Composable
fun LicenseRow(item: LicenseItem) {
    val context = LocalContext.current
    BasicListItem(
        headlineText = "${item.name} - ${item.author}",
        supportingText = "${item.url}\n${getLicense(item.type)}"
    ) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.url)))
    }
}