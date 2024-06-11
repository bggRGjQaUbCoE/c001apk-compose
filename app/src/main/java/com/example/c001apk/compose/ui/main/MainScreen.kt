package com.example.c001apk.compose.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.c001apk.compose.ui.component.SlideTransition
import com.example.c001apk.compose.ui.home.HomeScreen
import com.example.c001apk.compose.ui.message.MessageScreen
import com.example.c001apk.compose.ui.settings.SettingsScreen

/**
 * Created by bggRGjQaUbCoE on 2024/5/30
 */
@Composable
fun MainScreen(
    badge: Int,
    resetBadge: () -> Unit,
    onParamsClick: () -> Unit,
    onAboutClick: () -> Unit,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
    onSearch: () -> Unit,
    onOpenLink: (String) -> Unit,
    onCopyText: (String?) -> Unit,
    onViewApp: (String) -> Unit,
    onLogin: () -> Unit,
) {

    val screens = listOf(
        Router.HOME,
        Router.MESSAGE,
        Router.SETTINGS
    )

    var selectIndex by rememberSaveable { mutableIntStateOf(0) }
    val snackbarHostState = remember(::SnackbarHostState)
    val configuration = LocalConfiguration.current
    val savableStateHolder = rememberSaveableStateHolder()
    var refreshState by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(top = 0),
        bottomBar = {
            NavigationBar {
                screens.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    androidx.compose.animation.AnimatedVisibility(
                                        visible = if (index == 1) badge > 0
                                        else false,
                                        enter = scaleIn(animationSpec = tween(250)),
                                        exit = scaleOut(animationSpec = tween(250))
                                    ) {
                                        Badge(
                                            modifier = Modifier
                                                .padding(start = 15.dp, bottom = 10.dp)
                                        ) {
                                            Text(text = badge.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector =
                                    if (selectIndex == screens.indexOf(screen)) {
                                        screen.selectedIcon!!
                                    } else {
                                        screen.unselectedIcon!!
                                    },
                                    contentDescription = null
                                )
                            }
                        },
                        label = {
                            Text(text = stringResource(id = screen.stringId!!))
                        },
                        selected = selectIndex == screens.indexOf(screen),
                        onClick = {
                            with(screens.indexOf(screen)) {
                                if (selectIndex == 0 && this == 0) {
                                    refreshState = true
                                } else if (this == 1 && badge != 0) {
                                    resetBadge()
                                }
                                selectIndex = this
                            }
                        },
                        alwaysShowLabel = false
                    )
                }
            }
        },
        content = { paddingValues ->
            AnimatedContent(
                label = "home-content",
                targetState = selectIndex,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                transitionSpec = {
                    SlideTransition.slideLeft.enterTransition()
                        .togetherWith(SlideTransition.slideLeft.exitTransition())
                },
            ) { page ->
                savableStateHolder.SaveableStateProvider(
                    key = page,
                    content = {
                        when (page) {
                            0 -> HomeScreen(
                                refreshState = refreshState,
                                onRefresh = {
                                    refreshState = true
                                },
                                resetRefreshState = {
                                    refreshState = false
                                },
                                onViewUser = onViewUser,
                                onViewFeed = onViewFeed,
                                onSearch = onSearch,
                                onOpenLink = onOpenLink,
                                onCopyText = onCopyText,
                                onViewApp = onViewApp,
                            )

                            1 -> MessageScreen(
                                onLogin = onLogin,
                                onViewUser = onViewUser,
                                onViewFeed = onViewFeed,
                                onOpenLink = onOpenLink,
                                onCopyText = onCopyText,
                            )

                            2 -> SettingsScreen(
                                onParamsClick = onParamsClick,
                                onAboutClick = onAboutClick,
                            )
                        }
                    }
                )

            }
        },
    )

    BackHandler(enabled = selectIndex != 0) {
        selectIndex = 0
    }

}