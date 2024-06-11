package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.ThumbUpOffAlt
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.ui.component.IconText
import com.example.c001apk.compose.ui.component.LinkText
import com.example.c001apk.compose.ui.component.NineImageView
import com.example.c001apk.compose.util.DateUtils.fromToday
import com.example.c001apk.compose.util.Utils.richToString
import com.example.c001apk.compose.util.longClick

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedCard(
    modifier: Modifier = Modifier,
    isFeedContent: Boolean,
    data: HomeFeedResponse.Data,
    onViewUser: (String) -> Unit,
    onViewFeed: (String, String?) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
) {
    val horizontal = if (isFeedContent) 16.dp else 10.dp
    val vertical = if (isFeedContent) 12.dp else 10.dp
    Column(
        modifier = run {
            val tmp = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(
                    if (isFeedContent) RectangleShape
                    else RoundedCornerShape(12.0.dp)
                )
                .background(
                    if (isFeedContent) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            if (isFeedContent)
                tmp.longClick {
                    onCopyText(data.message)
                }
            else
                tmp.combinedClickable(
                    onClick = {
                        onViewFeed(data.id.orEmpty(), null)
                    },
                    onLongClick = {
                        onCopyText(data.message)
                    }
                )
        }
    ) {
        FeedHeader(
            modifier = Modifier.padding(start = horizontal),
            data = data,
            onViewUser = onViewUser,
            isFeedContent = isFeedContent,
        )
        FeedMessage(
            modifier = Modifier.padding(horizontal = horizontal),
            data = data,
            onOpenLink = onOpenLink
        )
        FeedBottomInfo(
            modifier = Modifier
                .padding(horizontal = horizontal)
                .padding(bottom = if (data.targetRow == null && data.relationRows.isNullOrEmpty()) 12.dp else 0.dp),
            isFeedContent = isFeedContent,
            ip = data.ipLocation.orEmpty(),
            dateline = data.dateline ?: 0,
            replyNum = data.replynum.orEmpty(),
            likeNum = data.likenum.orEmpty()
        )
        FeedRows(
            modifier = Modifier.padding(bottom = 10.dp),
            isFeedContent = isFeedContent,
            relationRows = data.relationRows,
            targetRow = data.targetRow,
            onOpenLink = onOpenLink
        )
    }
}

@Composable
fun FeedRows(
    modifier: Modifier = Modifier,
    isFeedContent: Boolean,
    relationRows: List<HomeFeedResponse.RelationRows>?,
    targetRow: HomeFeedResponse.TargetRow?,
    onOpenLink: (String, String?) -> Unit
) {
    val dataList = relationRows?.toMutableList() ?: ArrayList()
    targetRow?.let {
        dataList.add(
            0,
            HomeFeedResponse.RelationRows(
                it.id.orEmpty(),
                it.logo,
                it.title,
                it.url,
                it.targetType.toString()
            )
        )
    }

    if (dataList.isNotEmpty()) {
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = if (isFeedContent) 16.dp else 10.dp)
        ) {
            dataList.forEach {
                item(key = it.id) {
                    IconMiniScrollCardItem(
                        isFeedContent = isFeedContent,
                        logoUrl = it.logo.orEmpty(),
                        linkUrl = it.url,
                        titleText = it.title.orEmpty(),
                        onOpenLink = onOpenLink
                    )
                }
            }
        }
    }

}

@Composable
fun FeedBottomInfo(
    modifier: Modifier = Modifier,
    isFeedContent: Boolean,
    ip: String,
    dateline: Long,
    replyNum: String,
    likeNum: String
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = if (isFeedContent) {
                if (ip.isNotEmpty()) "发布于 $ip"
                else EMPTY_STRING
            } else fromToday(dateline),
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            color = MaterialTheme.colorScheme.outline
        )

        IconText(
            imageVector = Icons.AutoMirrored.Outlined.Message,
            title = replyNum,
            onClick = {
                // TODO: view feed reply
            }
        )

        IconText(
            modifier = Modifier.padding(start = 10.dp),
            imageVector = Icons.Default.ThumbUpOffAlt,
            title = likeNum,
            onClick = {
                // TODO: like feed
            }
        )
    }

}

@Composable
fun FeedMessage(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onOpenLink: (String, String?) -> Unit
) {

    if (!data.messageTitle.isNullOrEmpty()) {
        LinkText(
            text = data.messageTitle,
            textSize = 16f,
            isBold = true,
            lineSpacingMultiplier = 1.3f,
            modifier = modifier.padding(top = 10.dp),
            onOpenLink = onOpenLink
        )
    }
    if (!data.message.isNullOrEmpty()) {
        LinkText(
            text = data.message,
            textSize = 16f,
            lineSpacingMultiplier = 1.3f,
            modifier = modifier.padding(top = 10.dp),
            onOpenLink = onOpenLink
        )
    }

    if (!data.picArr.isNullOrEmpty()) {
        NineImageView(
            modifier = modifier.padding(top = 10.dp),
            pic = data.pic,
            picArr = data.picArr,
            feedType = data.feedType
        )
    }

    /*if (!data.picArr.isNullOrEmpty()) {

        val imageWidth = (minOf(screenWidth, screenHeight) - 46 * density) / 3f / density

        val column = when (data.picArr.size) {
            in 1..3 -> 1
            in 4..6 -> 2
            in 7..9 -> 3
            else -> 0
        }

        val context = LocalContext.current
        LazyVerticalGrid(
            modifier = Modifier
                .padding(top = 10.dp)
                .height((column * imageWidth).dp + 3.dp * (column - 1)),
            columns = GridCells.Fixed(3)
        ) {
            itemsIndexed(data.picArr) { index, item ->

                val startPadding = if (index % 3 == 0) 0.dp else 3.dp
                val topPadding = if (index > 2) 3.dp else 0.dp

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("$item.s.jpg")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(start = startPadding, top = topPadding)
                        .clip(RoundedCornerShape(12.dp))
                        .size(imageWidth.dp)
                )
            }
        }
    }*/
}

@Composable
fun FeedHeader(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onViewUser: (String) -> Unit,
    isFeedContent: Boolean,
) {

    val vertical = if (isFeedContent) 12.dp else 10.dp
    val context = LocalContext.current
    var dropdownMenuExpanded by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (avatar, username, from, device, expand) = createRefs()

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(data.userAvatar)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = vertical)
                .clip(CircleShape)
                .aspectRatio(1f, false)
                .constrainAs(avatar) {
                    top.linkTo(username.top)
                    bottom.linkTo(device.bottom)
                    start.linkTo(parent.start)
                    height = Dimension.fillToConstraints
                }
                .clickable {
                    onViewUser(data.uid.orEmpty())
                }
        )

        Text(
            modifier = Modifier
                .padding(start = 10.dp, top = vertical)
                .constrainAs(username) {
                    start.linkTo(avatar.end)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            text = data.username.orEmpty(),
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (isFeedContent || !data.infoHtml.isNullOrEmpty()) {
            Text(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .constrainAs(from) {
                        start.linkTo(avatar.end)
                        top.linkTo(username.bottom)
                    },
                text = if (isFeedContent) fromToday(
                    data.dateline ?: 0
                ) else data.infoHtml.orEmpty(),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                color = MaterialTheme.colorScheme.outline,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconText(
            modifier = Modifier
                .padding(start = 10.dp)
                .constrainAs(device) {
                    start.linkTo(if (isFeedContent || !data.infoHtml.isNullOrEmpty()) from.end else avatar.end)
                    top.linkTo(username.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            imageVector = Icons.Default.Smartphone,
            title = data.deviceTitle.orEmpty().richToString(),
            textSize = 13f,
        )

        if (!isFeedContent) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f, false)
                    .constrainAs(expand) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(device.bottom)
                        height = Dimension.fillToConstraints
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

}