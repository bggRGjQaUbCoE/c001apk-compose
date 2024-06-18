package com.example.c001apk.compose.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.cards.ChatLeftCard
import com.example.c001apk.compose.ui.component.cards.ChatRightCard
import com.example.c001apk.compose.ui.component.cards.ChatTimeCard
import com.example.c001apk.compose.ui.component.cards.LoadingCard
import com.example.c001apk.compose.util.CookieUtil
import com.example.c001apk.compose.util.ReportType
import com.example.c001apk.compose.util.copyText
import com.example.c001apk.compose.util.makeToast
import kotlinx.coroutines.delay

/**
 * Created by bggRGjQaUbCoE on 2024/6/19
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onBackClick: () -> Unit,
    ukey: String,
    uid: String,
    username: String,
    onViewUser: (String) -> Unit,
    onReport: (String, ReportType) -> Unit,
) {

    val viewModel =
        hiltViewModel<ChatViewModel, ChatViewModel.ViewModelFactory>(key = ukey) { factory ->
            factory.create(ukey = ukey)
        }

    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    var dropdownMenuExpanded by remember { mutableStateOf(false) }
    val dataList = remember(key1 = viewModel.loadingState) {
        (viewModel.loadingState as? LoadingState.Success)?.response ?: emptyList()
    }
    var textInput by remember { mutableStateOf(EMPTY_STRING) }
    var showDialog by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = viewModel.scroll) {
        if (viewModel.scroll) {
            textInput = EMPTY_STRING
            delay(150)
            lazyListState.scrollToItem(0)
            viewModel.reset()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton { onBackClick() }
                },
                title = { Text(text = username) },
                actions = {
                    Box {
                        IconButton(onClick = { dropdownMenuExpanded = true }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = dropdownMenuExpanded,
                            onDismissRequest = { dropdownMenuExpanded = false }) {
                            listOf("Check", "Block", "Report").forEachIndexed { index, menu ->
                                DropdownMenuItem(
                                    text = { Text(text = menu) },
                                    onClick = {
                                        dropdownMenuExpanded = false
                                        when (index) {
                                            0 -> onViewUser(uid)
                                            1 -> viewModel.onBlockUser(uid)
                                            2 -> onReport(uid, ReportType.USER)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = paddingValues.calculateLeftPadding(layoutDirection),
                    end = paddingValues.calculateRightPadding(layoutDirection),
                )
        ) {
            HorizontalDivider()

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                reverseLayout = true,
                state = lazyListState,
            ) {
                when (viewModel.loadingState) {
                    LoadingState.Loading, LoadingState.Empty, is LoadingState.Error -> {
                        item(key = "loadingState") {
                            Box(modifier = Modifier.fillParentMaxSize()) {
                                LoadingCard(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(horizontal = 10.dp),
                                    state = viewModel.loadingState,
                                    onClick = if (viewModel.loadingState is LoadingState.Loading) null
                                    else viewModel::loadMore
                                )
                            }
                        }
                    }

                    is LoadingState.Success -> {
                        itemsIndexed(
                            items = dataList,
                            key = { index, item -> item.entityId + index },
                        ) { index, item ->
                            when (item.entityType) {
                                "message" -> when (item.fromuid) {
                                    CookieUtil.uid ->
                                        ChatRightCard(
                                            data = item,
                                            onGetImageUrl = { id ->
                                                viewModel.onGetImageUrl(id)
                                            },
                                            onLongClick = { id, msg, url ->
                                                viewModel.deleteId = id
                                                viewModel.message = msg
                                                viewModel.pic = url
                                                showDialog = true
                                            },
                                            onViewUser = onViewUser,
                                        )

                                    else ->
                                        ChatLeftCard(
                                            data = item,
                                            onGetImageUrl = { id ->
                                                viewModel.onGetImageUrl(id)
                                            },
                                            onLongClick = { id, msg, url ->
                                                viewModel.deleteId = id
                                                viewModel.message = msg
                                                viewModel.pic = url
                                                showDialog = true
                                            },
                                            onViewUser = onViewUser,
                                        )
                                }

                                "messageExtra" -> ChatTimeCard(title = item.title.orEmpty())
                            }

                            if (index == dataList.lastIndex && !viewModel.isEnd) {
                                viewModel.loadMore()
                            }
                        }
                    }
                }
            }

            ChatBottom(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                textInput = textInput,
                onValueChange = { textInput = it },
                onPickImage = {

                },
                onSendMessage = {
                    viewModel.onSendMessage(uid, textInput, EMPTY_STRING)
                }
            )
        }

    }

    when {
        showDialog -> {
            Dialog(onDismissRequest = { showDialog = false }) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.elevatedCardColors()
                        .copy(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "删除",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showDialog = false
                                    viewModel.onDeleteMsg()
                                }
                                .padding(horizontal = 24.dp, vertical = 14.dp),
                            style = MaterialTheme.typography.titleSmall
                        )
                        if (viewModel.pic.isEmpty()) {
                            Text(
                                text = "复制",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showDialog = false
                                        context.copyText(viewModel.message)
                                    }
                                    .padding(horizontal = 24.dp, vertical = 14.dp),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        }
    }

    viewModel.toastText?.let {
        context.makeToast(it)
        viewModel.resetToastText()
    }

}

@Composable
fun ChatBottom(
    modifier: Modifier = Modifier,
    textInput: String,
    onValueChange: (String) -> Unit,
    onPickImage: () -> Unit,
    onSendMessage: () -> Unit,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clickable {

                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = Icons.Outlined.EmojiEmotions,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
        TextField(
            value = textInput,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = "写私信...")
            },
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 6.dp),
            maxLines = 4,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .clickable {
                    if (textInput.isEmpty())
                        onPickImage()
                    else
                        onSendMessage()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector =
                if (textInput.isEmpty())
                    Icons.Outlined.AddPhotoAlternate
                else
                    Icons.AutoMirrored.Default.Send,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}