package com.example.c001apk.compose.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.example.c001apk.compose.util.composeClick

/**
 * Created by bggRGjQaUbCoE on 2024/6/2
 */
@Composable
fun BackButton(
    onBackClick: () -> Unit
) {
    IconButton(onClick = composeClick {
        onBackClick()
    }) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = null
        )
    }
}