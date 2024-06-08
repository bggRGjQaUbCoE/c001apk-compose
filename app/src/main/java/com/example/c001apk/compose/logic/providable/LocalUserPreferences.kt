package com.example.c001apk.compose.logic.providable

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.c001apk.compose.logic.datastore.UserPreferencesCompat

val LocalUserPreferences = staticCompositionLocalOf { UserPreferencesCompat.default() }
