package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.ui.component.LinkText
import com.example.c001apk.compose.util.DateUtils

/**
 * Created by bggRGjQaUbCoE on 2024/6/11
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationCard(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onViewUser: (String) -> Unit,
    onOpenLink: (String, String?) -> Unit,
) {
    val context = LocalContext.current
    var dropdownMenuExpanded by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            .combinedClickable(
                onClick = {
                    // TODO: view
                },
                onLongClick = {
                    // TODO: delete
                }
            )
    ) {

        val (avatar, username, expand, message, dateLine) = createRefs()

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(data.fromUserAvatar)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)
                .size(30.dp)
                .clip(CircleShape)
                .constrainAs(avatar) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .clickable {
                    onViewUser(data.uid.orEmpty())
                }
        )

        Text(
            text = data.fromusername.orEmpty(),
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                .constrainAs(username) {
                    start.linkTo(avatar.end)
                    top.linkTo(parent.top)
                    end.linkTo(expand.start)
                    width = Dimension.fillToConstraints
                },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        LinkText(
            text = data.note.orEmpty(),
            modifier = Modifier
                .padding(start = 10.dp, top = 5.dp, end = 10.dp)
                .constrainAs(message) {
                    start.linkTo(avatar.end)
                    top.linkTo(username.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            lineSpacingMultiplier = 1.2f,
            onOpenLink = onOpenLink,
        )

        Text(
            text = DateUtils.fromToday(data.dateline ?: 0),
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                .constrainAs(dateLine) {
                    start.linkTo(avatar.end)
                    top.linkTo(message.bottom)
                },
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            color = MaterialTheme.colorScheme.outline
        )

        Box(
            modifier = Modifier
                .constrainAs(expand) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }) {

            IconButton(
                onClick = {
                    dropdownMenuExpanded = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }

            DropdownMenu(
                expanded = dropdownMenuExpanded,
                onDismissRequest = {
                    dropdownMenuExpanded = false
                },
            ) {
                listOf("Report", "Block").forEachIndexed { index, menu ->
                    DropdownMenuItem(
                        text = { Text(menu) },
                        onClick = {
                            dropdownMenuExpanded = false

                        }
                    )
                }
            }

        }

    }
}