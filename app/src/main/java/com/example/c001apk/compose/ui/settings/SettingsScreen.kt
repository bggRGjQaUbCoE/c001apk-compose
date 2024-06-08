package com.example.c001apk.compose.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Feed
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AllInclusive
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DeveloperMode
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.InvertColors
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.BuildConfig
import com.example.c001apk.compose.R
import com.example.c001apk.compose.ThemeMode
import com.example.c001apk.compose.logic.providable.LocalUserPreferences
import com.example.c001apk.compose.ui.component.HtmlText
import com.example.c001apk.compose.ui.component.settings.BasicListItem
import com.example.c001apk.compose.ui.component.settings.DropdownListItem
import com.example.c001apk.compose.ui.component.settings.SelectionItem
import com.example.c001apk.compose.ui.component.settings.SwitchListItem
import com.example.c001apk.compose.util.CacheDataManager.clearAllCache
import com.example.c001apk.compose.util.CacheDataManager.getTotalCacheSize
import com.example.c001apk.compose.util.PrefManager
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.util.Formatter

/**
 * Created by bggRGjQaUbCoE on 2024/5/30
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onParamsClick: () -> Unit,
    onAboutClick: () -> Unit,
) {

    val userPreferences = LocalUserPreferences.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val rememberScrollState = rememberScrollState()
    val layoutDirection = LocalLayoutDirection.current
    val context = LocalContext.current
    val imageQualityList by lazy { listOf("Auto", "Origin", "Thumbnail") }
    var dropdownMenuExpanded by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showSZLMIDDialog by remember { mutableStateOf(false) }
    var showFontScaleDialog by remember { mutableStateOf(false) }
    var showContentScaleDialog by remember { mutableStateOf(false) }
    var showImageQualityDialog by remember { mutableStateOf(false) }
    var showCleanCacheDialog by remember { mutableStateOf(false) }
    var cacheSize by remember { mutableStateOf(getTotalCacheSize(context)) }
    var imageQuality by remember { mutableIntStateOf(PrefManager.imageQuality) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumTopAppBar(
                title = { Text(text = "Settings") },
                actions = {
                    Box(Modifier.wrapContentSize(Alignment.TopEnd)) {
                        IconButton(onClick = { dropdownMenuExpanded = true }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = Icons.Default.MoreVert.name
                            )
                        }

                        DropdownMenu(
                            expanded = dropdownMenuExpanded,
                            onDismissRequest = { dropdownMenuExpanded = false }
                        ) {
                            listOf("Feed Back", "About").forEachIndexed { index, menu ->
                                DropdownMenuItem(
                                    text = { Text(menu) },
                                    onClick = {
                                        dropdownMenuExpanded = false
                                        when (index) {
                                            0 -> context.startActivity(
                                                Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("https://github.com/bggRGjQaUbCoE/c001apk-compose/issues")
                                                )
                                            )

                                            1 -> showAboutDialog = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = paddingValues.calculateLeftPadding(layoutDirection),
                    end = paddingValues.calculateRightPadding(layoutDirection)
                )
                .verticalScroll(rememberScrollState)
        ) {

            BasicListItem(leadingText = "c001apk-compose")
            BasicListItem(
                leadingImageVector = Icons.Outlined.Smartphone,
                headlineText = "SZLM ID",
                supportingText = userPreferences.szlmId.ifEmpty { null }
            ) {
                showSZLMIDDialog = true
            }
            BasicListItem(
                leadingImageVector = Icons.Outlined.DeveloperMode,
                headlineText = "Developer Mode",
            ) {
                onParamsClick()
            }

            BasicListItem(leadingText = "Theme")
            SwitchListItem(
                value = userPreferences.materialYou,
                leadingImageVector = Icons.Outlined.Palette,
                headlineText = "Material You",
            ) {
                viewModel.setMaterialYou(it)
            }
            DropdownListItem(
                leadingImageVector = Icons.Outlined.DarkMode,
                headlineText = "Theme",
                value = userPreferences.themeMode.name,
                selections = (0..2).map {
                    SelectionItem(ThemeMode.entries[it].name, ThemeMode.entries[it].name)
                }/*ThemeMode.entries.map {
                    SelectionItem(it.name, it.name)
                }*/,
                onValueChanged = { index, _ ->
                    viewModel.setDarkTheme(ThemeMode.forNumber(index))
                }
            )
            SwitchListItem(
                value = userPreferences.pureBlack,
                leadingImageVector = Icons.Outlined.InvertColors,
                headlineText = "Pure Black",
            ) {
                viewModel.setPureBlack(it)
            }

            BasicListItem(leadingText = "Display")
            BasicListItem(
                leadingImageVector = Icons.Outlined.Block,
                headlineText = "User BlackList",
            ) {
            }
            BasicListItem(
                leadingImageVector = Icons.Outlined.Block,
                headlineText = "Topic BlackList",
            ) {
            }
            BasicListItem(
                leadingImageVector = Icons.Outlined.TextFields,
                headlineText = "Font Scale",
                supportingText = "${Formatter().format("%.2f", userPreferences.fontScale)}x"
            ) {
                showFontScaleDialog = true
            }
            /*BasicListItem(
                leadingImageVector = Icons.Outlined.ImageAspectRatio,
                headlineText = "Content Scale",
                supportingText = "${Formatter().format("%.2f", userPreferences.contentScale)}x"
            ) {
                showContentScaleDialog = true
            }*/
            DropdownListItem(
                leadingImageVector = Icons.Outlined.Image,
                headlineText = "Image Quality",
                value = imageQuality,
                selections = listOf("Auto", "Origin", "Thumbnail").mapIndexed { index, label ->
                    SelectionItem(label, index)
                },
                onValueChanged = { index, _ ->
                    // viewModel.setImageQuality(index)
                    PrefManager.imageQuality = index
                    imageQuality = index
                }
            )
            /*BasicListItem(
                leadingImageVector = Icons.Outlined.Image,
                headlineText = "Image Quality",
                supportingText = imageQualityList[userPreferences.imageQuality]
            ) {
                showImageQualityDialog = true
            }*/
            SwitchListItem(
                value = userPreferences.imageFilter,
                leadingImageVector = Icons.Outlined.PhotoLibrary,
                headlineText = "Image Filter",
            ) {
                viewModel.setImageFilter(it)
            }
            SwitchListItem(
                value = userPreferences.openInBrowser,
                leadingImageVector = Icons.Outlined.TravelExplore,
                headlineText = "Open In Browser",
            ) {
                viewModel.setOpenInBrowser(it)
            }
            SwitchListItem(
                value = userPreferences.showSquare,
                leadingImageVector = Icons.AutoMirrored.Outlined.Feed,
                headlineText = "Show Square",
            ) {
                viewModel.setShowSquare(it)
            }
            SwitchListItem(
                value = userPreferences.recordHistory,
                leadingImageVector = Icons.Outlined.History,
                headlineText = "Record History",
            ) {
                viewModel.setRecordHistory(it)
            }
            SwitchListItem(
                value = userPreferences.showEmoji,
                leadingImageVector = Icons.Outlined.EmojiEmotions,
                headlineText = "Show Emoji",
            ) {
                viewModel.setShowEmoji(it)
            }
            SwitchListItem(
                value = userPreferences.checkUpdate,
                leadingImageVector = Icons.Outlined.SystemUpdate,
                headlineText = "Check Update",
            ) {
                viewModel.setCheckUpdate(it)
            }
            SwitchListItem(
                value = userPreferences.checkCount,
                leadingImageVector = Icons.Outlined.Notifications,
                headlineText = "Check Count",
            ) {
                viewModel.setCheckCount(it)
            }


            BasicListItem(leadingText = "Others")
            BasicListItem(
                leadingImageVector = Icons.Outlined.AllInclusive,
                headlineText = "About",
                supportingText = "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"
            ) {
                onAboutClick()
            }
            BasicListItem(
                leadingImageVector = Icons.Outlined.CleaningServices,
                headlineText = "Clean Cache",
                supportingText = cacheSize
            ) {
                cacheSize = getTotalCacheSize(context)
                showCleanCacheDialog = true
            }

        }

        when {
            showAboutDialog -> {
                AboutDialog {
                    showAboutDialog = false
                }
            }

            showImageQualityDialog -> {
                ListItemDialog(
                    title = "Image Quality",
                    list = imageQualityList,
                    selected = userPreferences.imageQuality,
                    onDismiss = {
                        showImageQualityDialog = false
                    },
                    setData = {
                        showImageQualityDialog = false
                        viewModel.setImageQuality(it)
                    }
                )
            }

            showSZLMIDDialog -> {
                EditTextDialog(
                    data = userPreferences.szlmId,
                    title = "SZLM ID",
                    onDismiss = {
                        showSZLMIDDialog = false
                    },
                    setData = {
                        viewModel.setSZLMId(it)
                    }
                )
            }

            showFontScaleDialog -> {
                SliderDialog(
                    data = userPreferences.fontScale,
                    title = "Font Scale",
                    hint = "Font Size",
                    onDismiss = {
                        showFontScaleDialog = false
                    },
                    setData = {
                        viewModel.setFontScale(it)
                    }
                )
            }

            showContentScaleDialog -> {
                SliderDialog(
                    data = userPreferences.contentScale,
                    title = "Content Scale",
                    hint = "Content Size",
                    onDismiss = {
                        showContentScaleDialog = false
                    },
                    setData = {
                        viewModel.setContentScale(it)
                    }
                )
            }

            showCleanCacheDialog -> {
                CleanCacheDialog(
                    cacheSize = cacheSize,
                    onDismiss = {
                        showCleanCacheDialog = false
                    },
                    onClean = {
                        clearAllCache(context)
                        cacheSize = "Cleaned Up"
                    }
                )
            }

        }
    }

}

@Composable
fun CleanCacheDialog(
    cacheSize: String,
    onDismiss: () -> Unit,
    onClean: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }) {
                Text(text = "Close")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onClean()
                }
            ) {
                Text(text = "OK")
            }
        },
        text = {
            Text(text = "current cache size: $cacheSize")
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Clean Cache",
                textAlign = TextAlign.Center
            )
        }
    )
}

@Composable
fun ListItemDialog(
    title: String,
    list: List<String>,
    selected: Int,
    onDismiss: () -> Unit,
    setData: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {},
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                textAlign = TextAlign.Center
            )
        },
        text = {
            LazyColumn {
                itemsIndexed(list) { index, title ->
                    ListItemDialogItem(title, selected == index) {
                        setData(index)
                    }
                }
            }
        },
    )
}

@Composable
fun ListItemDialogItem(title: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.clickable { onClick() }
    ) {
        RadioButton(selected = selected, onClick = { onClick() })
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
            text = title,
        )
    }
}

@Composable
fun EditTextDialog(
    data: String,
    title: String,
    onDismiss: () -> Unit,
    setData: (String) -> Unit
) {
    var text by remember {
        mutableStateOf(
            TextFieldValue(
                text = data,
                selection = TextRange(data.length)
            )
        )
    }
    val focusRequest = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        try {
            focusRequest.requestFocus()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }) {
                Text(text = "Close")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    setData(text.text)
                }) {
                Text(text = "OK")
            }
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                textAlign = TextAlign.Center
            )
        },
        text = {
            OutlinedTextField(
                modifier = Modifier.focusRequester(focusRequest),
                value = text,
                onValueChange = { text = it },
            )
        },
    )
}

@Composable
fun SliderDialog(
    data: Float,
    title: String,
    hint: String,
    onDismiss: () -> Unit,
    setData: (Float) -> Unit
) {
    var progress by remember { mutableFloatStateOf(data) }
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    setData(1.0f)
                }) {
                Text(text = "Reset")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    setData(progress)
                }) {
                Text(text = "OK")
            }
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                Modifier.fillMaxWidth()
            ) {
                Slider(
                    value = progress,
                    onValueChange = {
                        progress = it
                    },
                    valueRange = 0.8f..1.3f
                )
                Text(
                    text = "$hint: ${Formatter().format("%.2f", progress)}",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 15.sp * progress,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    Dialog(onDismissRequest = { onDismiss() }) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                val drawable = ResourcesCompat.getDrawable(
                    context.resources,
                    R.mipmap.ic_launcher,
                    context.theme
                )
                Image(
                    painter = rememberDrawablePainter(drawable),
                    contentDescription = "icon",
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {

                    Text(
                        stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    HtmlText(
                        html = stringResource(
                            id = R.string.about_source_code,
                            "<b><a href=\"https://github.com/bggRGjQaUbCoE/c001apk-compose\">GitHub</a></b>"
                        )
                    )
                }
            }
        }
    }
}