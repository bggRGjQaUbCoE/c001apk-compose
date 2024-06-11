package com.example.c001apk.compose.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.outlined.AllInclusive
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Source
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.example.c001apk.compose.BuildConfig
import com.example.c001apk.compose.R
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.settings.BasicListItem
import com.google.accompanist.drawablepainter.rememberDrawablePainter

/**
 * Created by bggRGjQaUbCoE on 2024/6/2
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    onLicenseClick: () -> Unit,
) {

    val context = LocalContext.current
    val rememberScrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton { onBackClick() }
                },
                title = { Text(text = "About") },
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                val drawable = ResourcesCompat.getDrawable(
                    context.resources,
                    R.mipmap.ic_launcher,
                    context.theme
                )
                Image(
                    painter = rememberDrawablePainter(drawable),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            HorizontalDivider()
            BasicListItem(
                leadingImageVector = Icons.Outlined.AllInclusive,
                headlineText = "c001apk-compose",
                supportingText = "test only"
            ) { }
            BasicListItem(
                leadingImageVector = Icons.Default.ErrorOutline,
                headlineText = "Version",
                supportingText = "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"
            ) { }
            BasicListItem(
                leadingImageVector = Icons.Outlined.Code,
                headlineText = "Source Code",
                supportingText = "https://github.com/bggRGjQaUbCoE/c001apk-compose"
            ) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/bggRGjQaUbCoE/c001apk-compose")
                    )
                )
            }
            BasicListItem(
                leadingImageVector = Icons.Outlined.Source,
                headlineText = "Open Source License"
            ) {
                onLicenseClick()
            }

        }
    }

}