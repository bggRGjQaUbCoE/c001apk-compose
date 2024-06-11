package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.ExpandMore
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.ui.component.CoilLoader
import com.example.c001apk.compose.ui.component.IconText
import com.example.c001apk.compose.ui.component.LinkText
import com.example.c001apk.compose.ui.component.NineImageView
import com.example.c001apk.compose.util.DateUtils.fromToday

/**
 * Created by bggRGjQaUbCoE on 2024/6/6
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedReplyCard(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onViewUser: (String) -> Unit,
    onShowTotalReply: (String, String) -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
) {

    var dropdownMenuExpanded by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    // TODO: reply
                },
                onLongClick = {
                    onCopyText(data.message)
                }
            )
            .padding(start = 16.dp, bottom = 12.dp)
    ) {

        val (avatar, username, expand, message, image, dateLine, reply, like, replyRows) = createRefs()

        CoilLoader(
            url = data.userAvatar,
            modifier = Modifier
                .padding(top = 12.dp)
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

        LinkText(
            text = data.username.orEmpty(),
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 12.dp)
                .constrainAs(username) {
                    start.linkTo(avatar.end)
                    top.linkTo(parent.top)
                    end.linkTo(expand.start)
                    width = Dimension.fillToConstraints
                },
            maxLines = if (data.replyRows == null) null else 1,
            onOpenLink = onOpenLink,
        )

        LinkText(
            text = data.message.orEmpty(),
            modifier = Modifier
                .padding(start = 10.dp, top = 5.dp, end = 16.dp)
                .constrainAs(message) {
                    start.linkTo(avatar.end)
                    top.linkTo(username.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            lineSpacingMultiplier = 1.2f,
            onOpenLink = onOpenLink,
        )

        if (!data.picArr.isNullOrEmpty()) {
            NineImageView(
                pic = data.pic,
                picArr = data.picArr,
                feedType = data.feedType,
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp, end = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .constrainAs(image) {
                        start.linkTo(avatar.end)
                        top.linkTo(message.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            )
        }

        Text(
            text = fromToday(data.dateline ?: 0),
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)
                .constrainAs(dateLine) {
                    start.linkTo(avatar.end)
                    top.linkTo(
                        if (data.picArr.isNullOrEmpty()) message.bottom
                        else image.bottom
                    )
                },
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            color = MaterialTheme.colorScheme.outline
        )

        IconText(
            modifier = Modifier
                .padding(end = 10.dp, top = 10.dp)
                .constrainAs(reply) {
                    top.linkTo(
                        if (data.picArr.isNullOrEmpty()) message.bottom
                        else image.bottom
                    )
                    end.linkTo(like.start)
                },
            imageVector = Icons.AutoMirrored.Outlined.Message,
            title = data.replynum.orEmpty(),
        )

        IconText(
            modifier = Modifier
                .padding(top = 10.dp, end = 16.dp)
                .constrainAs(like) {
                    top.linkTo(
                        if (data.picArr.isNullOrEmpty()) message.bottom
                        else image.bottom
                    )
                    end.linkTo(parent.end)
                },
            imageVector = Icons.Default.ThumbUpOffAlt,
            title = data.likenum.orEmpty(),
            onClick = {
                // TODO: like reply
            }
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
                listOf("Report", "Block", "Show Reply").forEachIndexed { index, menu ->
                    DropdownMenuItem(
                        text = { Text(menu) },
                        onClick = {
                            dropdownMenuExpanded = false
                            when (index) {
                                2 -> onShowTotalReply(data.id.orEmpty(), data.uid.orEmpty())
                            }
                        }
                    )
                }
            }

        }

        if (!data.replyRows.isNullOrEmpty()) {
            ReplyRows(
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp, end = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                    .constrainAs(replyRows) {
                        top.linkTo(dateLine.bottom)
                        start.linkTo(avatar.end)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                data = data.replyRows!!,
                replyRowsMore = data.replyRowsMore ?: 0,
                replyNum = data.replynum ?: EMPTY_STRING,
                onShowTotalReply = {
                    onShowTotalReply(data.id.orEmpty(), data.uid.orEmpty())
                },
                onOpenLink = onOpenLink,
                onCopyText = onCopyText,
            )
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReplyRows(
    modifier: Modifier = Modifier,
    data: List<HomeFeedResponse.ReplyRows>,
    replyRowsMore: Int,
    replyNum: String,
    onShowTotalReply: () -> Unit,
    onOpenLink: (String, String?) -> Unit,
    onCopyText: (String?) -> Unit,
) {

    var dropdownMenuExpanded by remember { mutableIntStateOf(-1) }

    Column(
        modifier = modifier
    ) {
        data.forEachIndexed { index, reply ->
            Box {
                LinkText(
                    text = reply.message.orEmpty(),
                    lineSpacingMultiplier = 1.2f,
                    textSize = 14f,
                    onOpenLink = onOpenLink,
                    isReply = true,
                    onShowTotalReply = {
                        onShowTotalReply()
                    },
                    imgList = reply.picArr,
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                // TODO: reply
                            },
                            onLongClick = {
                                dropdownMenuExpanded = index
                            }
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
                DropdownMenu(
                    expanded = dropdownMenuExpanded == index,
                    onDismissRequest = { dropdownMenuExpanded = -1 }
                ) {
                    listOf("Copy", "Report", "BLock", "Show Reply").forEachIndexed { index, menu ->
                        DropdownMenuItem(
                            text = { Text(menu) },
                            onClick = {
                                dropdownMenuExpanded = -1
                                when (index) {
                                    0 -> onCopyText(reply.message)

                                    3 -> onShowTotalReply()
                                }
                            }
                        )
                    }
                }
            }

        }
        if (replyRowsMore != 0) {
            LinkText(
                text = "查看更多回复($replyNum)",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onShowTotalReply()
                    }
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.primary.toArgb(),
                textSize = 14f,
                onOpenLink = onOpenLink,
            )
        }
    }


}
