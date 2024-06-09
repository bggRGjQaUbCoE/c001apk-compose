package com.example.c001apk.compose.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.c001apk.compose.logic.providable.LocalUserPreferences
import com.example.c001apk.compose.logic.repository.UserPreferencesRepository
import com.example.c001apk.compose.ui.theme.C001apkComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by bggRGjQaUbCoE on 2024/5/29
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var navController: NavHostController
    private val viewModel by viewModels<MainViewModel>()

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        intent.data?.let {
            navController.onOpenLink(this, it.toString(), true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            navController = rememberNavController()

            val userPreferences by userPreferencesRepository.data
                .collectAsStateWithLifecycle(
                    initialValue = null,
                    lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
                )

            val preferences = if (userPreferences == null) {
                return@setContent
            } else {
                checkNotNull(userPreferences)
            }

            CompositionLocalProvider(
                LocalUserPreferences provides preferences
            ) {
                C001apkComposeTheme(
                    darkTheme = preferences.isDarkMode(),
                    materialYou = preferences.materialYou,
                    pureBlack = preferences.pureBlack,
                    fontScale = preferences.fontScale,
                    contentScale = preferences.contentScale,
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        MainNavigation(
                            navController = navController,
                            badge = viewModel.badge,
                            resetBadge = viewModel::resetBadge
                        )
                    }
                }
            }

            LaunchedEffect(key1 = navController) {
                if (savedInstanceState == null) {
                    handleIntent(intent)
                }
            }

        }

    }
}
