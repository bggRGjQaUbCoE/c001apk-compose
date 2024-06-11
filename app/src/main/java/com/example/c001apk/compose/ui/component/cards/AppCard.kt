package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
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
import com.example.c001apk.compose.util.DateUtils.fromToday

/**
 * Created by bggRGjQaUbCoE on 2024/6/10
 */

enum class AppCardType {
    APP, PRODUCT, TOPIC, USER
}

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onOpenLink: (String) -> Unit,
    appCardType: AppCardType
) {

    val context = LocalContext.current

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            .clickable {
                onOpenLink(data.url.orEmpty())
            }
            .padding(10.dp)
    ) {

        val (logo, appName, commentNum, downCount, active, follow) = createRefs()

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(
                    when (appCardType) {
                        AppCardType.APP, AppCardType.PRODUCT, AppCardType.TOPIC -> data.logo
                        AppCardType.USER -> data.userAvatar
                    }
                )
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .aspectRatio(1f)
                .constrainAs(logo) {
                    start.linkTo(parent.start)
                    top.linkTo(appName.top)
                    bottom.linkTo(commentNum.bottom)
                    height = Dimension.fillToConstraints
                }
        )

        Text(
            text = when (appCardType) {
                AppCardType.APP, AppCardType.PRODUCT, AppCardType.TOPIC -> data.title.orEmpty()
                AppCardType.USER -> data.username.orEmpty()
            },
            modifier = Modifier
                .padding(start = 10.dp, end = if (appCardType == AppCardType.USER) 10.dp else 0.dp)
                .constrainAs(appName) {
                    start.linkTo(logo.end)
                    top.linkTo(parent.top)
                    end.linkTo(if (appCardType == AppCardType.USER) follow.start else parent.end)
                    width = Dimension.fillToConstraints
                },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = when (appCardType) {
                AppCardType.APP -> "${data.commentnum}讨论"
                AppCardType.PRODUCT, AppCardType.TOPIC -> "${data.hotNumTxt}热度"
                AppCardType.USER -> "${data.follow}关注"
            },
            modifier = Modifier
                .padding(start = 10.dp)
                .constrainAs(commentNum) {
                    start.linkTo(logo.end)
                    top.linkTo(appName.bottom)
                },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
        )

        Text(
            text = when (appCardType) {
                AppCardType.APP -> "${data.downCount}下载"
                AppCardType.PRODUCT -> "${data.feedCommentNumTxt}讨论"
                AppCardType.TOPIC -> "${data.commentnumTxt}讨论"
                AppCardType.USER -> "${data.fans}粉丝"
            },
            modifier = Modifier
                .padding(start = 10.dp)
                .constrainAs(downCount) {
                    start.linkTo(commentNum.end)
                    top.linkTo(appName.bottom)
                },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
        )

        if (appCardType == AppCardType.USER) {
            Text(
                text = "${fromToday(data.logintime ?: 0)}活跃",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .constrainAs(active) {
                        start.linkTo(downCount.end)
                        top.linkTo(appName.bottom)
                    },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            )

            TextButton(
                onClick = {
                    // TODO
                },
                modifier = Modifier
                    .constrainAs(follow) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                Text(text = if (data.isFollow == 1) "取消关注" else "关注")
            }
        }

    }

}