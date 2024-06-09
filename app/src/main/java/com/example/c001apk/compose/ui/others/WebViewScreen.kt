package com.example.c001apk.compose.ui.others

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.WebView
import com.example.c001apk.compose.util.decode
import com.example.c001apk.compose.util.openInBrowser
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/9
 */

enum class ActionType {
    REFRESH, COPY, OPEN, CLEAN, NONE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    onBackClick: () -> Unit,
    url: String,
    isLogin: Boolean = false,
) {

    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var dropdownMenuExpanded by remember { mutableStateOf(false) }

    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    )
    val snackbarHostState = remember(::SnackbarHostState)
    val scope = rememberCoroutineScope()

    var actionType by remember { mutableStateOf(ActionType.NONE) }
    var onBack by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton { onBackClick() }
                },
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = { dropdownMenuExpanded = true }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = dropdownMenuExpanded,
                            onDismissRequest = { dropdownMenuExpanded = false }
                        ) {
                            listOf("Refresh", "Copy", "Open in Browser", "Clean Caches")
                                .forEachIndexed { index, menu ->
                                    DropdownMenuItem(
                                        text = { Text(menu) },
                                        onClick = {
                                            dropdownMenuExpanded = false
                                            actionType = when (index) {
                                                0 -> ActionType.REFRESH
                                                1 -> ActionType.COPY
                                                2 -> ActionType.OPEN
                                                3 -> ActionType.CLEAN
                                                else -> ActionType.NONE
                                            }
                                        }
                                    )
                                }

                        }
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedVisibility(visible = progress != 100f) {
                LinearProgressIndicator(progress = { animatedProgress })
            }

            WebView(
                url = url.decode,
                isLogin = isLogin,
                onFinishLogin = {
                    // TODO: get tokens
                },
                actionType = actionType,
                resetActionType = {
                    actionType = ActionType.NONE
                },
                onFinish = onBackClick,
                onBack = onBack,
                onBackReset = {
                    onBack = false
                },
                onUpdateProgress = { progress = it },
                onUpdateTitle = { title = it },
                onShowSnackbar = { requestUrl ->
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "当前网页将要打开外部链接，是否打开",
                            actionLabel = "打开",
                            withDismissAction = true
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                context.openInBrowser(requestUrl)
                            }

                            SnackbarResult.Dismissed -> {}
                        }
                    }
                }
            )

        }

    }

    BackHandler {
        onBack = true
    }

}